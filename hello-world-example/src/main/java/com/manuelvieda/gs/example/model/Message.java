/**
 * PayU Latam - Copyright (c) 2013 - 2015
 * http://www.payu.com.co
 * Date: 24/07/2015
 */
package com.manuelvieda.gs.example.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.openspaces.core.cluster.ClusterInfo;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceClass.IncludeProperties;
import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.annotation.pojo.SpaceIndexes;
import com.gigaspaces.annotation.pojo.SpaceInitialLoadQuery;
import com.gigaspaces.annotation.pojo.SpaceLeaseExpiration;
import com.gigaspaces.annotation.pojo.SpaceProperty;
import com.gigaspaces.document.DocumentProperties;
import com.gigaspaces.metadata.index.SpaceIndexType;

/**
 * @author Manuel E. Vieda (contacto@manuelvieda.com)
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
	 * The lease time of the message
	 */
	private long lease;

	/**
	 * Message state
	 */
	public static final String STATE = "state";

	public static final String NEW_STATE_DATE = "newStateDate";
	public static final String IN_VALIDATION_STATE_DATE = "inValidationStateDate";
	public static final String SUBMITTED_STATE_DATE = "submittedStateDate";
	public static final String PROCESSED_STATE_DATE = "processedStateDate";

	/**
	 * Message contentn
	 */
	public static final String CONTENT = "content";

	/**
	 * The routing field name.
	 */
	private static final String ROUTING_FIELD = "id";

	/**
	 * Returns the properties (attributes) container
	 *
	 * @return the properties container
	 */
	@Override
	@Transient
	@SpaceIndexes(@SpaceIndex(path = STATE, type = SpaceIndexType.BASIC))
	@SpaceDynamicProperties
	public DocumentProperties getProperties() {

		return properties;
	}

	/*
	 * (non-Javadoc)
	 * @see com.manuelvieda.gs.example.model.BaseEntity#getId()
	 */
	@Id
	@Column(name = "id", unique = true, length = UUID_LENGTH, nullable = false)
	@Size(min = UUID_LENGTH, max = UUID_LENGTH)
	@SpaceId(autoGenerate = false)
	@Override
	@SpaceProperty
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

	/**
	 * Returns the message state
	 *
	 * @return
	 */
	@Column(name = "state", length = ENUM_LENGTH)
	@Enumerated(EnumType.STRING)
	public MessageState getState() {

		return get(STATE);
	}

	/**
	 * Return the date/time where the message changes to NEW state
	 * 
	 * @return The date where the message change to NEW state
	 */
	@Column(name = "new_state_date")
	public Date getNewStateDate() {

		return get(NEW_STATE_DATE);
	}

	/**
	 * Return the date/time where the message changes to IN_VALIDATION state
	 * 
	 * @return The date where the message change to IN_VALIDATION state
	 */
	@Column(name = "in_validation_state_date")
	public Date getInValidationSateDate() {

		return get(IN_VALIDATION_STATE_DATE);
	}

	/**
	 * Return the date/time where the message changes to SUBMITTED state
	 * 
	 * @return The date where the message change to SUBMITTED state
	 */
	@Column(name = "submitted_state_date")
	public Date getSubmittedStateDate() {

		return get(SUBMITTED_STATE_DATE);
	}

	/**
	 * Return the date/time where the message changes to PROCESSED state
	 * 
	 * @return The date where the message change to PROCESSED state
	 */
	@Column(name = "processed_state_date")
	public Date getProcessedStateDate() {

		return get(PROCESSED_STATE_DATE);
	}

	/**
	 *
	 * @return
	 */
	@Column(name = "content", length = 255)
	@Length(min = 1, max = 255)
	@NotEmpty
	public String getContent() {

		return get(CONTENT);
	}

	/*
	 * (non-Javadoc)
	 * @see com.manuelvieda.gs.example.model.BaseEntity#setId(java.io.Serializable)
	 */
	@Override
	public void setId(final String id) {

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
	 * Sets the message state
	 *
	 * @param state The message state to set
	 */
	public void setState(final MessageState state) {

		set(STATE, state);
	}

	/**
	 * Sets the date/time where the message change to NEW state
	 * 
	 * @param newStateDate The date where the message state changes
	 */
	public void setNewStateDate(final Date newStateDate) {

		set(NEW_STATE_DATE, newStateDate);
	}

	/**
	 * Sets the date/time where the message change to IN_VALIDATION state
	 * 
	 * @param newStateDate The date where the message state changes
	 */
	public void setInValidationSateDate(final Date inValidationStateDate) {

		set(IN_VALIDATION_STATE_DATE, inValidationStateDate);
	}

	/**
	 * Sets the date/time where the message change to SUBMITTED state
	 * 
	 * @param newStateDate The date where the message state changes
	 */
	public void setSubmittedStateDate(final Date submittedSateDate) {

		set(SUBMITTED_STATE_DATE, submittedSateDate);
	}

	/**
	 * Sets the date/time where the message change to PROCESSED state
	 * 
	 * @param newStateDate The date where the message state changes
	 */
	public void setProcessedStateDate(final Date processedStateDate) {

		set(PROCESSED_STATE_DATE, processedStateDate);
	}

	/**
	 * Sets the message content
	 *
	 * @param content The content to set
	 */
	public void setContent(final String content) {

		set(CONTENT, content);
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
