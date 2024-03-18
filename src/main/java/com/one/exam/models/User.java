package com.one.exam.models;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity{
	
	@NotNull
	@NotBlank
	@Length(min = 3, message = "Must be at least 3 characters")
	private String name;

	@NotNull
	@NotEmpty(message = "The email field is required")
    @Email(message = "Enter a valid email")
	@Column(unique = true)
	private String email;

	@NotNull
	@NotEmpty(message = "The password field is required")
    @Length(min = 6, message = "Must be at least 6 characters")
    private String password;
	
	@NotEmpty(message = "The confirmation field is required")
    @Transient
    private String passwordConfirmation;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
    private List<Mesa> mesas;
	
}
