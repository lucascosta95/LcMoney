package br.com.lucascosta.lcmoneyapi.repository.lancamento;

import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaCategoria;
import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaDia;
import br.com.lucascosta.lcmoneyapi.model.Categoria_;
import br.com.lucascosta.lcmoneyapi.model.Lancamento;
import br.com.lucascosta.lcmoneyapi.model.Lancamento_;
import br.com.lucascosta.lcmoneyapi.model.Pessoa_;
import br.com.lucascosta.lcmoneyapi.repository.filter.LancamentoFilter;
import br.com.lucascosta.lcmoneyapi.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

        Root<Lancamento> root = criteria.from(Lancamento.class);

        // Criar as restrições - Cri
        Predicate[] predicate = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicate);

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        adicionarRestricoesPaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);

        Root<Lancamento> root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(ResumoLancamento.class,
                root.get(Lancamento_.id),
                root.get(Lancamento_.descricao),
                root.get(Lancamento_.dataVencimento),
                root.get(Lancamento_.dataPagamento),
                root.get(Lancamento_.valor),
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.categoria).get(Categoria_.nome),
                root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

        Predicate[] predicate = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicate);

        TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
        adicionarRestricoesPaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));

    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) {
            predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao() + "%"));
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);

    }

    private void adicionarRestricoesPaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicate = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicate);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }


    public List<LancamentosEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LancamentosEstatisticaCategoria> criteria = builder.createQuery(LancamentosEstatisticaCategoria.class);

        Root<Lancamento> root = criteria.from(Lancamento.class);
        criteria.select(builder.construct(LancamentosEstatisticaCategoria.class, root.get(Lancamento_.categoria), builder.sum(root.get(Lancamento_.valor))));

        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
                builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia)
        );

        criteria.groupBy(root.get(Lancamento_.categoria));

        TypedQuery<LancamentosEstatisticaCategoria> typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }

    public List<LancamentosEstatisticaDia> porDia(LocalDate mesReferencia) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<LancamentosEstatisticaDia> criteria = builder.createQuery(LancamentosEstatisticaDia.class);

        Root<Lancamento> root = criteria.from(Lancamento.class);
        criteria.select(builder.construct(LancamentosEstatisticaDia.class,
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.dataVencimento),
                builder.sum(root.get(Lancamento_.valor))));

        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
                builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia)
        );

        criteria.groupBy(
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.dataVencimento));

        TypedQuery<LancamentosEstatisticaDia> typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }

}
