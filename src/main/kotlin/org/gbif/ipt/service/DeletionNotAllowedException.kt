package org.gbif.ipt.service

open class DeletionNotAllowedException : Exception {

  enum class Reason {
    /**
     * Because this is the last administrator. Used while deleting a user.
     */
    LAST_ADMIN,

    /**
     * Because this is the last resource manager. Used while deleting a user.
     */
    LAST_RESOURCE_MANAGER,

    /**
     * Because this is the original creator of the resource, which cannot be deleted. Used while deleting a user.
     */
    IS_RESOURCE_CREATOR,

    /**
     * Because the extension has been mapped in at least one resource. Used while deleting an extension.
     */
    EXTENSION_MAPPED,

    /**
     * Because the data schema has been mapped in at least one resource. Used while deleting an data schema.
     */
    DATA_SCHEMA_MAPPED,

    /**
     * Because the vocabulary is a default vocabulary. Used while deleting a vocabulary.
     */
    BASE_VOCABULARY,

    /**
     * Because there is at least one resource registered to this organization. Used while deleting an organization.
     */
    RESOURCE_REGISTERED_WITH_ORGANISATION,

    /**
     * Because the IPT is registered against this organization. Used while deleting an organization.
     */
    IPT_REGISTERED_WITH_ORGANISATION,

    /**
     * Because some registry error occurred.
     */
    REGISTRY_ERROR,

    /**
     * Because some DOI Registration Agency error occurred.
     */
    DOI_REGISTRATION_AGENCY_ERROR,

    /**
     * Because there is at least one resource whose DOI is registered with this organization. Used while deleting
     * an organization.
     */
    RESOURCE_DOI_REGISTERED_WITH_ORGANISATION
  }

  var reason: Reason? = null

  constructor(reason: Reason?) {
    this.reason = reason
  }

  constructor(reason: Reason?, message: String?): super(message) {
    this.reason = reason
  }
}
