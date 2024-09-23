package domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //jpa required
//@AllArgsConstructor -> wil zelf definiëren omdat we die autogen id hebben
@EqualsAndHashCode(of = "naam")
@Table(name = "discipline")
/**
 * Class Discipline
 * 
 * relationship link: An existing Sport + discipline-name required
 */
public class Discipline implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "discipline_id")
	private int id;
	
    @ManyToOne
    @JoinColumn(name = "sport_id")
    private Sport sport;

    private String naam;

    public Discipline(Sport sport, String naam) {
        this.sport = sport;
        this.naam = naam;
    }

}
