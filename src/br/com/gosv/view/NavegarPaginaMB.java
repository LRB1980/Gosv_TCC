package br.com.gosv.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class NavegarPaginaMB {
	
	public String home;

	public String home (){
		System.out.println("Deve ir para pagina home");
		return "menu?faces-redirect=true";
	}
	
	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	
}
