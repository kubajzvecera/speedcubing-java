package com.speedcubers.speedcubing.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Solve.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Solve_ {

	public static final String ATTEMPT_NUMBER = "attemptNumber";
	public static final String ROUND = "round";
	public static final String PENALTY = "penalty";
	public static final String COMPETITOR = "competitor";
	public static final String ID = "id";
	public static final String TIME_MS = "timeMs";

	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve#attemptNumber
	 **/
	public static volatile SingularAttribute<Solve, Integer> attemptNumber;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve#round
	 **/
	public static volatile SingularAttribute<Solve, Round> round;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve#penalty
	 **/
	public static volatile SingularAttribute<Solve, String> penalty;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve#competitor
	 **/
	public static volatile SingularAttribute<Solve, Competitor> competitor;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve#id
	 **/
	public static volatile SingularAttribute<Solve, Long> id;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve
	 **/
	public static volatile EntityType<Solve> class_;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Solve#timeMs
	 **/
	public static volatile SingularAttribute<Solve, Integer> timeMs;

}

