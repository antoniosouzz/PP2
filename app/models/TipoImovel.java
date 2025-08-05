package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class TipoImovel extends Model{

	public String descricao;
	public Status status;
	
	public TipoImovel(String descricao) {
		this.descricao = descricao;
		this.status = Status.ATIVO;
	}
	
	public TipoImovel() {
		this.status = Status.ATIVO;
	}
	
	@Override
	public String toString() {
		return descricao;
	}
	
}
