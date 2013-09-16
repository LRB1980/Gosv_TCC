package br.com.gosv.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

@ManagedBean (name="selectOneMB")
@RequestScoped
public class SelectOneMB implements Serializable {
	
	/**
	 * 
	 */
	private HtmlSelectOneRadio checkEstado = new HtmlSelectOneRadio();      
	private HtmlInputText checkInputTextEstado = new HtmlInputText();  
	private static final long serialVersionUID = 1L;
	
	public SelectOneMB() {      //Meu construtor    
	    checkEstado.setId("tipo");  
	    //checkInputTextEstado.setId("RazaoSocial"); 
	    checkInputTextEstado.setLabel("Teste");
	    checkInputTextEstado.setRendered(false);  
	}     
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void mudar(ValueChangeEvent e){
		System.out.println("Entrou aqui otario de uma Figa DESGRAÇADO");
		System.out.println("Imprimiu o Check : " + checkInputTextEstado);
		if(e.getNewValue().toString().equals("PF")){
			System.out.println("Entrou NO IF");
			System.out.println("Imprimiu o Check : " + checkInputTextEstado);
			checkInputTextEstado.setRendered(true);
		}else{
			checkInputTextEstado.setRendered(false);
		}
		FacesContext.getCurrentInstance().renderResponse();  
		}
	public void alterarValor(){  
	     System.out.println("Passei");
	  }  
	     

	public HtmlSelectOneRadio getCheckEstado() {
		return checkEstado;
	}

	public void setCheckEstado(HtmlSelectOneRadio checkEstado) {
		this.checkEstado = checkEstado;
	}

	public HtmlInputText getCheckInputTextEstado() {
		return checkInputTextEstado;
	}

	public void setCheckInputTextEstado(HtmlInputText checkInputTextEstado) {
		this.checkInputTextEstado = checkInputTextEstado;
	}


}
