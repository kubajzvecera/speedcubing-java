package com.speedcubers.speedcubing.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Result.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class Result_ {

	public static final String BEST_TIME = "bestTime";
	public static final String AVERAGE_TIME = "averageTime";
	public static final String ROUND = "round";
	public static final String COMPETITOR = "competitor";
	public static final String RANK = "rank";
	public static final String ID = "id";

	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result#bestTime
	 **/
	public static volatile SingularAttribute<Result, Integer> bestTime;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result#averageTime
	 **/
	public static volatile SingularAttribute<Result, Double> averageTime;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result#round
	 **/
	public static volatile SingularAttribute<Result, Round> round;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result#competitor
	 **/
	public static volatile SingularAttribute<Result, Competitor> competitor;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result#rank
	 **/
	public static volatile SingularAttribute<Result, Integer> rank;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result#id
	 **/
	public static volatile SingularAttribute<Result, Long> id;
	
	/**
	 * @see com.speedcubers.speedcubing.entity.Result
	 **/
	public static volatile EntityType<Result> class_;

}

