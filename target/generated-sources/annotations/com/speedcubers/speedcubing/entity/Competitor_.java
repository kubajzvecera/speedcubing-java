package com.speedcubers.speedcubing.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Competitor.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Competitor_ {

	public static final String COUNTRY = "country";
	public static final String NAME = "name";
	public static final String SOLVES = "solves";
	public static final String ID = "id";
	public static final String RESULTS = "results";

	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competitor#country
	 **/
	public static volatile SingularAttribute<Competitor, String> country;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competitor#name
	 **/
	public static volatile SingularAttribute<Competitor, String> name;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competitor#solves
	 **/
	public static volatile ListAttribute<Competitor, Solve> solves;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competitor#id
	 **/
	public static volatile SingularAttribute<Competitor, Long> id;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competitor
	 **/
	public static volatile EntityType<Competitor> class_;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Competitor#results
	 **/
	public static volatile ListAttribute<Competitor, Result> results;

}

