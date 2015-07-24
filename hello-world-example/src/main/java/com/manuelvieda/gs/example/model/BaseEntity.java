/**
 * PayU Latam - Copyright (c) 2013 - 2015
 * http://www.payu.com.co
 * Date: 24/07/2015
 */
package com.manuelvieda.gs.example.model;

import java.beans.Transient;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.openspaces.core.cluster.ClusterInfo;

import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.document.DocumentProperties;

/**
 * Base entity for all model classes
 *
 * @author Manuel E. Vieda (contacto@manuelvieda.com)</a>
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseEntity<T extends Serializable> implements Serializable {

	/**
	 * The default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of the database schema used in the persistent entities.
	 */
	public static final String DATABASE_SCHEMA = "gs";
	
	/**
	 * Length for UUIDs
	 */
	protected static final int UUID_LENGTH = 36;
	
	/**
	 * The space lease expiration field name
	 */
	protected static final String LEASE_FIELD = "lease";

	// --------------------------------------------------------------------
	// DOCUMENT PROPERTIES - GIGASPACES DYNAMIC ATTRIBUTES CONTAINER
	// --------------------------------------------------------------------

	/**
	 * Object attributes container
	 */
	protected DocumentProperties properties = new DocumentProperties();

	/**
	 * Returns the properties (attributes) container
	 *
	 * @return the properties container
	 */
	@Transient
	@SpaceDynamicProperties
	public DocumentProperties getProperties() {

		return properties;
	}

	/**
	 * Sets the properties (attributes) container
	 *
	 * @param properties the properties container to set
	 */
	public void setProperties(final DocumentProperties properties) {

		this.properties = properties;
	}

	/**
	 * Get document property value by name
	 *
	 * @param propertyName property name
	 * @return The property value
	 */
	protected <U> U get(final String propertyName) {

		return properties.getProperty(propertyName);
	}

	/**
	 * Set document property
	 *
	 * @param propertyName property name
	 * @param value property value to set
	 */
	protected void set(final String propertyName, final Object value) {

		properties.setProperty(propertyName, value);
	}

	/**
	 * Returns the object identifier
	 *
	 * @return The object identifier
	 */
	public abstract T getId();

	/**
	 * Sets the object identifier
	 *
	 * @param id The identifier to set
	 */
	public abstract void setId(final T id);

	/**
	 * Creates the Space/Database query for initial object load including the appropriate filters
	 * based on lease and routing (Partitions).
	 *
	 * <ul>
	 * <li>Lease filter: ABS(leaseFieldName) > CurrentTime(ms)
	 * <li>Routing Filter: ABS( routingField.hashCode ) % TotalPartitions == QueryPartition-1
	 * </ul>
	 *
	 * @param clusterInfo Cluster information
	 * @param leaseFieldName Expiration time lease filed name. <code>null</code> if the entity does
	 *            not define an expiration lease time.
	 * @param routingFieldName Routing field name. <code>null</code> if the entity does not define a
	 *            routing field.
	 *            (Note: Current version of GigaSpaces uses the idField for undefined routing Field)
	 * @param numericRouting Flag indicating if the routing field contains a numeric value. Non
	 *            numeric values have a special behavior.
	 *
	 * @return Space/Database query used by GigaSpaces to filer the space initialization
	 */
	protected static String createRoutingQuery(final ClusterInfo clusterInfo,
			final String leaseFieldName, final String routingFieldName,
			final boolean numericRouting) {

		final StringBuilder query = new StringBuilder();

		if (StringUtils.isNotBlank(leaseFieldName)) {
			query.append("ABS(").append(leaseFieldName).append(") >= ").append(System.currentTimeMillis());
		}

		if (clusterInfo != null && StringUtils.isNotBlank(routingFieldName)) {

			final int totalPartitions = clusterInfo.getNumberOfInstances();
			final int queryPartition = clusterInfo.getInstanceId() - 1;

			if (query.length() > 0) {
				query.append(" and ");
			}

			query.append(" MOD(");

			if (numericRouting) {
				query.append(routingFieldName);
			}
			else {
				query.append("ABS(hashCodeStringJava(").append(routingFieldName).append("))");
			}
			query.append(", ").append(totalPartitions).append(") = ").append(queryPartition);
		}

		return query.toString();
	}

	/**
	 * Creates the Space/Database query for initial object load including the appropriate filters
	 * based on lease time.
	 *
	 * <ul>
	 * <li>Lease filter: ABS(leaseFieldName) > CurrentTime(ms)
	 * </ul>
	 *
	 * @param clusterInfo Cluster information
	 * @param leaseFieldName Expiration time lease filed name. <code>null</code> if the entity does
	 *            not define an expiration lease time.
	 * 
	 * @return Space/Database query used by GigaSpaces to filer the space initialization
	 */
	protected String createRoutingQuery(final ClusterInfo clusterInfo, final String leaseFieldName) {

		return createRoutingQuery(clusterInfo, leaseFieldName, null, false);
	}

}
