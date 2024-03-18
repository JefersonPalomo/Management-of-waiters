package com.one.exam.models;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mesas")
@Getter
@Setter
@NoArgsConstructor
public class Mesa extends BaseEntity{

	@NotNull
	@NotBlank
	@Length(min = 2, message = "Must be at least 2 characters")
	private String guestName;
	
	@NotNull
	@Min(value = 1, message = "Must have at least 1 guest")
	@Max(value = 10, message = "Must have at least 10 guest")
	private int guests;
	
	private String notes;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
	
}
