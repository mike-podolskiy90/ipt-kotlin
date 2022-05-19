package org.gbif.ipt.service

class InvalidConfigException : RuntimeException {

  enum class TYPE {
    /**
     * Invalid Base URL specified.
     */
    INVALID_BASE_URL,

    /**
     * Base URL specified is inaccessible.
     */
    INACCESSIBLE_BASE_URL,

    /**
     * Data directory is invalid.
     */
    INVALID_DATA_DIR,

    /**
     * Data directory is not writable.
     */
    NON_WRITABLE_DATA_DIR,

    /**
     * Configuration changes failed to be written.
     */
    CONFIG_WRITE,

    /**
     * User account configuration could not be read.
     */
    USER_CONFIG,

    /**
     * Extension is invalid for some reason. For example, it has XML breaking characters and can't be parsed.
     */
    INVALID_EXTENSION,

    /**
     * Vocabulary is invalid for some reason. For example, it has XML breaking characters and can't be parsed.
     */
    INVALID_VOCABULARY,

    /**
     * Data schema is invalid for some reason. For example, it JSON can't be parsed.
     */
    INVALID_DATA_SCHEMA,

    /**
     * The IPT mode (test or production) has been set, and this cannot change.
     */
    DATADIR_ALREADY_REGISTERED,

    /**
     * Visibility change not permitted: registered status is final.
     */
    RESOURCE_ALREADY_REGISTERED,

    /**
     * Registration configuration cannot be read.
     */
    REGISTRATION_CONFIG,

    /**
     * Registration configuration has a bad configuration (e.g. 2 DOI accounts activated, when only 1 allowed).
     */
    REGISTRATION_BAD_CONFIG,

    /**
     * Resource configuration cannot be read.
     */
    RESOURCE_CONFIG,

    /**
     * An EML template exception has been encountered.
     */
    EML,

    /**
     * Proxy specified is invalid.
     */
    INVALID_PROXY,

    /**
     * Latitude/longitude are in invalid format.
     */
    FORMAT_ERROR,

    /**
     * An extension with given rowType has already been installed.
     */
    ROWTYPE_ALREADY_INSTALLED,

    /**
     * The resource cannot be migrated as configured.
     */
    INVALID_RESOURCE_MIGRATION,

    /**
     * The resource DOI cannot be registered as configured.
     */
    INVALID_DOI_REGISTRATION,

    /**
     * The resource cannot be migrated as configured.
     */
    AUTO_PUBLISHING_ALREADY_OFF,

    /**
     * Once a DOI registration agency account has been activated in the IPT, archival mode cannot be turned off fx.
     */
    DOI_REGISTRATION_ALREADY_ACTIVATED,

    /**
     * A properties file has been configured wrong.
     */
    INVALID_PROPERTIES_FILE
  }

  private var type: TYPE? = null

  constructor(type: TYPE?, message: String?): super(message) {
    this.type = type
  }

  constructor(type: TYPE?, message: String?, e: Exception?): super(message, e) {
    this.type = type
  }
}
