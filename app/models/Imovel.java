package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import play.db.jpa.Model;

@Entity
public class Imovel extends Model{

	public String codigoAnuncio;
	public String bairro;
	public int quantidadeComodos;
	public double areaInterna;
	public double areaExterna;
	
	@ManyToOne
	public TipoImovel tipoImovel;
	
	public Imovel(String codigoAnuncio, String bairro, int quantidadeComodos, double areaInterna, double areaExterna, TipoImovel tipoImovel) {
        this.codigoAnuncio = codigoAnuncio;
        this.bairro = bairro;
        this.quantidadeComodos = quantidadeComodos;
        this.areaInterna = areaInterna;
        this.areaExterna = areaExterna;
        this.tipoImovel = tipoImovel;
    }
	
	@Transient
	public double areaTotal() {
		return this.areaInterna + this.areaExterna;
	}
}
