export class Endereco {
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cep?: string;
  cidade?: string;
  estado?: string;
}

export class Contato {
  id?: number;
  nome?: string;
  email?: string;
  telefone?: string;

  constructor(id?: number, nome?: string, email?: string, telefone?: string) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.telefone = telefone;
  }
}

export class Pessoa {
  id?: number;
  nome?: string;
  endereco = new Endereco();
  ativo = true;
  contatos = new Array<Contato>();
}

export class Categoria {
  id?: number;
}

export class Lancamento {
  id?: number;
  tipo = 'RECEITA';
  descricao?: string;
  dataVencimento?: Date;
  dataPagamento?: Date;
  valor?: number;
  observacao?: string;
  pessoa = new Pessoa();
  categoria = new Categoria();
}
