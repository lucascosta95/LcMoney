package br.com.lucascosta.lcmoneyapi.repository.lancamento;

import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaCategoria;
import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaDia;
import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaPessoa;
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
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(Lancamento.class);
        var root = criteria.from(Lancamento.class);

        // Criar as restrições - Cri
        var predicate = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicate);

        var query = manager.createQuery(criteria);
        adicionarRestricoesPaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(ResumoLancamento.class);
        var root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(ResumoLancamento.class,
                root.get(Lancamento_.id),
                root.get(Lancamento_.descricao),
                root.get(Lancamento_.dataVencimento),
                root.get(Lancamento_.dataPagamento),
                root.get(Lancamento_.valor),
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.categoria).get(Categoria_.nome),
                root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

        var predicate = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicate);

        var query = manager.createQuery(criteria);
        adicionarRestricoesPaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));

    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
        var predicates = new ArrayList<>();

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
        var paginaAtual = pageable.getPageNumber();
        var totalRegistrosPorPagina = pageable.getPageSize();
        var primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(LancamentoFilter lancamentoFilter) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(Long.class);
        var root = criteria.from(Lancamento.class);

        var predicate = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicate);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }


    public List<LancamentosEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(LancamentosEstatisticaCategoria.class);
        var root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(LancamentosEstatisticaCategoria.class, root.get(Lancamento_.categoria), builder.sum(root.get(Lancamento_.valor))));

        var primeiroDia = mesReferencia.withDayOfMonth(1);
        var ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
                builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia)
        );

        criteria.groupBy(root.get(Lancamento_.categoria));

        var typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }

    public List<LancamentosEstatisticaDia> porDia(LocalDate mesReferencia) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(LancamentosEstatisticaDia.class);

        var root = criteria.from(Lancamento.class);
        criteria.select(builder.construct(LancamentosEstatisticaDia.class,
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.dataVencimento),
                builder.sum(root.get(Lancamento_.valor))));

        var primeiroDia = mesReferencia.withDayOfMonth(1);
        var ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), primeiroDia),
                builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), ultimoDia)
        );

        criteria.groupBy(
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.dataVencimento));

        var typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }


    public List<LancamentosEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim) {
        var builder = manager.getCriteriaBuilder();
        var criteria = builder.createQuery(LancamentosEstatisticaPessoa.class);

        var root = criteria.from(Lancamento.class);
        criteria.select(builder.construct(LancamentosEstatisticaPessoa.class,
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.pessoa),
                builder.sum(root.get(Lancamento_.valor))));

        criteria.where(
                builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), inicio),
                builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), fim)
        );

        criteria.groupBy(
                root.get(Lancamento_.tipo),
                root.get(Lancamento_.pessoa));

        var typedQuery = manager.createQuery(criteria);

        return typedQuery.getResultList();
    }

}
