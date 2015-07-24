/**
 * PayU Latam - Copyright (c) 2013 - 2015
 * http://www.payu.com.co
 * Date: 24/07/2015
 */
package com.manuelvieda.gs.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.openspaces.core.cluster.ClusterInfo;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceClass.IncludeProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceInitialLoadQuery;
import com.gigaspaces.annotation.pojo.SpaceLeaseExpiration;

/**
 * @author Manuel E. Vieda  (contacto@manuelvieda.com)
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "message", schema = BaseEntity.DATABASE_SCHEMA)
@SpaceClass(includeProperties = IncludeProperties.EXPLICIT)
public class Message extends BaseEntity<String> {
	
	/**
	 * The class UID version.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Message identifier code (UUID)
	 */
	private String id;
	
	/**
	 * The routing field name.
	 */
	private static final String ROUTING_FIELD = "id";
	
	/**
	 * The lease time of the message
	 */
	private long lease;

	/*
	 * (non-Javadoc)
	 * @see com.manuelvieda.gs.example.model.BaseEntity#getId()
	 */
	@Id
	@Column(name = "transaccion_id", unique = true, length = UUID_LENGTH, nullable = false)
	@Size(min = UUID_LENGTH, max = UUID_LENGTH)
	@SpaceId(autoGenerate = false)
	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the object lease time.
	 * 
	 * @return the object lease time.
	 */
	@SpaceLeaseExpiration
	public long getLease() {

		return lease;
	}

	/*
	 * (non-Javadoc)
	 * @see com.manuelvieda.gs.example.model.BaseEntity#setId(java.io.Serializable)
	 */
	@Override
	public void setId(String id) {

		this.id = id;
	}
	
	/**
	 * Sets the object lease time
	 * 
	 * @param lease the lease to set
	 */
	public void setLease(final long lease) {

		this.lease = lease;
	}
	
	/**
	 * Creates the initial load query
	 * 
	 * @param clusterInfo GigaSpaces cluster information object
	 * @return Initial load query
	 */
	@SpaceInitialLoadQuery
	public String initialLoadQuery(final ClusterInfo clusterInfo) {

		return createRoutingQuery(clusterInfo, LEASE_FIELD, ROUTING_FIELD, false);
	}

}
