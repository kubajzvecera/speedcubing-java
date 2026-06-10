package com.speedcubers.speedcubing.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Round.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Round_ {

	public static final String ROUND_NUMBER = "roundNumber";
	public static final String NAME = "name";
	public static final String SOLVES = "solves";
	public static final String COMPETITION = "competition";
	public static final String ID = "id";
	public static final String RESULTS = "results";

	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round#roundNumber
	 **/
	public static volatile SingularAttribute<Round, Integer> roundNumber;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round#name
	 **/
	public static volatile SingularAttribute<Round, String> name;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round#solves
	 **/
	public static volatile ListAttribute<Round, Solve> solves;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round#competition
	 **/
	public static volatile SingularAttribute<Round, Competition> competition;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round#id
	 **/
	public static volatile SingularAttribute<Round, Long> id;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round
	 **/
	public static volatile EntityType<Round> class_;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Round#results
	 **/
	public static volatile ListAttribute<Round, Result> results;

}

