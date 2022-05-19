package org.gbif.ipt.config

import org.gbif.ipt.model.User

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission

@Lazy
@Configuration
class XstreamConfig {

  @Bean
  fun xstream(): XStream {
    val xstream = XStream()
    xstream.addPermission(AnyTypePermission.ANY)
    xstream.alias("user", User::class.java)
    xstream.useAttributeFor(User::class.java, "email")
    xstream.useAttributeFor(User::class.java, "password")
    xstream.useAttributeFor(User::class.java, "firstname")
    xstream.useAttributeFor(User::class.java, "lastname")
    xstream.useAttributeFor(User::class.java, "role")
    xstream.useAttributeFor(User::class.java, "lastLogin")
    return xstream
  }
}
