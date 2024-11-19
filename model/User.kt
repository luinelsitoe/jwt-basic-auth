package com.luinel.notas_kt

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    var id: Int? = null,

    @Column(nullable = false)
    var fullName: String,

    @Column(unique = true, length = 100, nullable = false)
    var email: String = "teste",

    @Column(nullable = false)
    private var password: String,

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    var createdAt: Date? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Date? = null
) : UserDetails {
    constructor() : this(null, "", "", "", null, null)

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    fun setPassword(password: String){
        this.password = password
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
