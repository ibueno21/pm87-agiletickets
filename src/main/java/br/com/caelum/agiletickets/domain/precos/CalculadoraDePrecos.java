package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;
		
		TipoDeEspetaculo tipoDeEspetaculo = sessao.getEspetaculo().getTipo();
		
		
		if(tipoDeEspetaculo.equals(TipoDeEspetaculo.CINEMA) || tipoDeEspetaculo.equals(TipoDeEspetaculo.SHOW)) {
			
			if(calculaPorcentagemDeIngressos(sessao) <= 0.05 ) { 
				preco = acrescentaPorcentagem( sessao, 0.10 ) ;
			} else {
				preco = sessao.getPreco();
			}
		} else if(tipoDeEspetaculo.equals(TipoDeEspetaculo.BALLET)) {
			if(calculaPorcentagemDeIngressos(sessao) <= 0.50) { 
				preco =  acrescentaPorcentagem( sessao,0.20);
			} else {
				preco = sessao.getPreco();
			}
			 preco= alteraPrecoDuracaoSessao(sessao, preco);
			
		} else if(tipoDeEspetaculo.equals(TipoDeEspetaculo.ORQUESTRA)) {
			if(calculaPorcentagemDeIngressos(sessao) <= 0.50) { 
				preco = acrescentaPorcentagem( sessao, 0.20);
			} else {
				preco = sessao.getPreco();
			}

			preco= alteraPrecoDuracaoSessao(sessao, preco);
			
		}  else {
			//nao aplica aumento para teatro (quem vai é pobretão)
			preco = sessao.getPreco();
		} 

		return preco.multiply(BigDecimal.valueOf(quantidade));
	}
	
	private static double calculaPorcentagemDeIngressos(Sessao s){
		return (s.getTotalIngressos() - s.getIngressosReservados()) / s.getTotalIngressos().doubleValue();
	}
	
	private static BigDecimal acrescentaPorcentagem(Sessao s, double valor){
		return s.getPreco().add(s.getPreco().multiply(BigDecimal.valueOf(valor)));
	}

	private static BigDecimal alteraPrecoDuracaoSessao(Sessao s, BigDecimal p){
		if(s.getDuracaoEmMinutos() > 60){
			return p.add(s.getPreco().multiply(BigDecimal.valueOf(0.10)));
		}
		return p;
	}
}