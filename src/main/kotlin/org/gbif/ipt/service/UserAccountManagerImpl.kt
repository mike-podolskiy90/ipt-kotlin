package org.gbif.ipt.service

import org.gbif.ipt.model.User
import org.gbif.ipt.utils.PERSISTENCE_FILE
import org.gbif.ipt.utils.getUtf8Reader
import org.gbif.ipt.utils.startNewUtf8File
import java.io.EOFException
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Locale
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.thoughtworks.xstream.XStream

@Service
class UserAccountManagerImpl : UserAccountManager {

  private val logger = LoggerFactory.getLogger(UserAccountManagerImpl::class.java)

  @Autowired
  private lateinit var xstream: XStream
  @Autowired
  private lateinit var dataDir: DataDir
  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  private var allowSimplifiedAdminLogin = true
  private var onlyAdminEmail: String? = null

  private var users: MutableMap<String, User> = LinkedHashMap()

  @PostConstruct
  fun init() {
    if (users.isEmpty()) {
      load()
    }
  }

  override fun authenticate(email: String?, password: String?): User? {
    TODO("Not yet implemented")
  }

  @Throws(AlreadyExistingException::class, IOException::class)
  override fun create(user: User?) {
    if (user != null) {
      if (get(user.email) != null) {
        throw AlreadyExistingException()
      }
      // hash password before creation
      user.password = passwordEncoder.encode(user.password)
      addUser(user)
      save()
    }
  }

  @Throws(DeletionNotAllowedException::class, IOException::class)
  override fun delete(email: String?): User? {
    if (email != null) {
      val remUser = get(email)
      if (remUser != null) {

        // when deleting an admin, ensure another admin still exists
        if (remUser.role === User.Role.Admin) {
          var lastAdmin = true
          for (u in users.values) {
            if (u.role === User.Role.Admin && u != remUser) {
              lastAdmin = false
              break
            }
          }
          if (lastAdmin) {
            logger.warn("Last admin cannot be deleted")
            throw DeletionNotAllowedException(DeletionNotAllowedException.Reason.LAST_ADMIN)
          }
        }
//        val resourcesCreatedByUser: MutableSet<String> = HashSet()
//        for (r in resourceManager.list()) {
//          val creator = get(r.getCreator().getEmail())
//          if (creator != null && creator.equals(remUser)) {
//            resourcesCreatedByUser.add(r.getShortname())
//          }
//        }
//        val resourcesManagedOnlyByUser: MutableSet<String> = HashSet()
//        for (r in resourceManager.list(remUser)) {
//          val managers: MutableSet<User> = HashSet()
//          // add creator to list of managers, but only if creator has manager rights!
//          val creator = get(r.getCreator().getEmail())
//          if (creator != null && creator.hasManagerRights()) {
//            managers.add(creator)
//          }
//          for (m in r.getManagers()) {
//            val manager = get(m.getEmail())
//            if (manager != null && !managers.contains(manager)) {
//              managers.add(manager)
//            }
//          }
//          // lastly, exclude user to be deleted, then check if at least one user with manager rights remains for resource
//          managers.remove(remUser)
//          if (managers.isEmpty()) {
//            resourcesManagedOnlyByUser.add(r.getShortname())
//          }
//        }
//        if (!resourcesManagedOnlyByUser.isEmpty()) {
//          // Check #1, is user the only manager that exists for or more resources? If yes, prevent deletion!
//          throw DeletionNotAllowedException(Reason.LAST_RESOURCE_MANAGER, resourcesManagedOnlyByUser.toString())
//        } else if (!resourcesCreatedByUser.isEmpty()) {
//          // Check #2, is user the creator of one or more resources? If yes, prevent deletion!
//          throw DeletionNotAllowedException(Reason.IS_RESOURCE_CREATOR, resourcesCreatedByUser.toString())
//        } else
        if (remove(email)) {
          // and remove user from each resource's list of managers
//          for (r in resourceManager.list(remUser)) {
//            r.getManagers().remove(remUser)
//            resourceManager.save(r)
//          }
          save() // persist changes to users.xml
          return remUser
        }
      }
    }
    return null
  }

  fun remove(email: String?): Boolean {
    return if (email != null && users.containsKey(email.lowercase(Locale.getDefault()))) {
      users.remove(email.lowercase(Locale.getDefault())) != null
    } else false
  }

  override fun get(email: String?): User? {
    return if (email != null && users.containsKey(email.lowercase(Locale.getDefault()))) {
      users[email.lowercase(Locale.getDefault())]
    } else null
  }

  override fun list(): List<User?>? {
    val userList: ArrayList<User?> = ArrayList(users.values)
    userList.sortBy { it!!.firstname + it.lastname }
    return userList
  }

  override fun list(role: User.Role?): List<User?> {
    val matchingUsers: MutableList<User> = ArrayList()
    for (u in users.values) {
      if (u.role === role) {
        matchingUsers.add(u)
      }
    }
    return matchingUsers
  }

  @Throws(InvalidConfigException::class)
  override fun load() {
    try {
      xstream.createObjectInputStream(
        getUtf8Reader(dataDir.configFile(PERSISTENCE_FILE)!!)
      ).use { `in` ->
        users.clear()
        while (true) {
          try {
            val u = `in`.readObject() as User
            addUser(u)
          } catch (e: EOFException) {
            // end of file, expected exception!
            break
          } catch (e: ClassNotFoundException) {
            logger.error(e.message, e)
          }
        }

//        // first we have to check if there are users with old-fashioned passwords
//        val isOldPasswordsPresent =
//          users.values
//            .map { it.password }
//            .any { StringUtils.startsWith(it, "$2a$") }
//
//        // if so - update all passwords
//        if (isOldPasswordsPresent) {
//          TODO("update old passwords!")
//        }
      }
    } catch (e: FileNotFoundException) {
      logger.warn(
        "User accounts not existing, " + PERSISTENCE_FILE
            + " file missing  (This is normal when first setting up a new datadir)"
      )
    } catch (e: IOException) {
      logger.error(e.message, e)
      throw InvalidConfigException(InvalidConfigException.TYPE.USER_CONFIG, "Couldnt read user accounts: " + e.message)
    }
  }

  @Synchronized
  @Throws(IOException::class)
  override fun save() {
    logger.debug("Saving all " + users.size + " user accounts...")
    startNewUtf8File(dataDir.configFile(PERSISTENCE_FILE)!!)
      .use { userWriter ->
        xstream.createObjectOutputStream(userWriter, "users")
          .use { out ->
            for ((_, value) in users) {
              out.writeObject(value)
            }
          }
      }
  }

  @Throws(IOException::class)
  override fun save(user: User?) {
    if (user != null) {
      val storedUser = get(user.email)

      if (storedUser != null) {
        storedUser.firstname = user.firstname
        storedUser.lastname = user.lastname
        storedUser.role = user.role
        user.password?.let { storedUser.password = it }
        addUser(storedUser)
        save()
      }
    }
  }

  override fun getDefaultAdminEmail(): String? {
    TODO("Not yet implemented")
  }

  override fun getSetupUser(): User? {
    TODO("Not yet implemented")
  }

  override fun setSetupUser(setupLogin: User?) {
    TODO("Not yet implemented")
  }

  override fun isLastAdmin(email: String?): Boolean {
    val user = get(email)
    return user?.role === User.Role.Admin && list(User.Role.Admin).size < 2
  }

  private fun addUser(user: User?): User? {
    if (user != null) {
      if (user.role === User.Role.Admin) {
        logger.debug("Adding admin " + user.email)
        if (allowSimplifiedAdminLogin) {
          if (onlyAdminEmail == null || users.isEmpty()) {
            // first admin - keep its email address available for simplified login
            // without email address but keyword "admin"
            onlyAdminEmail = user.email
          } else {
            // at least 2 admins exist - disable simplified admin login
            onlyAdminEmail = null
            allowSimplifiedAdminLogin = false
          }
        }
      } else {
        logger.debug("Adding user " + user.email)
      }
      users[user.email!!.lowercase()] = user
    }
    return user
  }
}
