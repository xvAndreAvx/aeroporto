package com.controller;

import java.io.Serializable;

public class Navigazione implements Serializable{

private static final long serialVersionUID = 1L;

public String reindirizza(String page ) {
return page + "?faces-redirect=true";
}

public String rimaniQui(String page) {
return page + "?faces-redirect=false" ;
}

}