package com.speedcubers.speedcubing.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(Competition.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Competition_ {

	public static final String DATE = "date";
	public static final String NAME = "name";
	public static final String LOCATION = "location";
	public static final String ID = "id";
	public static final String ROUNDS = "rounds";

	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competition#date
	 **/
	public static volatile SingularAttribute<Competition, LocalDate> date;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competition#name
	 **/
	public static volatile SingularAttribute<Competition, String> name;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competition#location
	 **/
	public static volatile SingularAttribute<Competition, String> location;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competition#id
	 **/
	public static volatile SingularAttribute<Competition, Long> id;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competition
	 **/
	public static volatile EntityType<Competition> class_;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competition#rounds
	 **/
	public static volatile ListAttribute<Competition, Round> rounds;

}

