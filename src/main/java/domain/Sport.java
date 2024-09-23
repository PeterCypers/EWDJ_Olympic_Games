package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//TODO: testen
@NamedQueries({
    @NamedQuery(
        name = "Sport.findSportByWedstrijdId",
        query = "SELECT s " +
                "FROM Sport s " +
                "JOIN s.wedstrijden w " +
                "WHERE w.wedstrijdId = :wedstrijdId "
    )
})
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "naam")
@Table(name = "sport")
public class Sport implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "sport_id")
	private int sportId;
	
	@OneToMany(mappedBy = "sport"/*, cascade = CascadeType.ALL*/)
	private List<Wedstrijd> wedstrijden = new ArrayList<>();
	
	@OneToMany(mappedBy = "sport"/*, cascade = CascadeType.ALL*/)
    private Set<Discipline> disciplines = new HashSet<>();
	
	private String naam;
	private String image;
	
//	public Sport(int sportId, String naam, String image, Set<Discipline> disciplines) {
//		this.sportId = sportId;
//		this.naam = naam;
//		this.image = image;
//		this.disciplines = disciplines;
//	}
	
	public Sport(int sportId, String naam, String image) {
		this.sportId = sportId;
		this.naam = naam;
		this.image = image;
	}
	
	public void addWedstrijd(Wedstrijd wedstrijd) {
		//relatie aan andere kant goed zetten:
		wedstrijd.setSport(this);
		
		wedstrijden.add(wedstrijd);
	}
	
    public void addDiscipline(Discipline discipline) {
        // Relatie aan andere kant goed zetten:
        discipline.setSport(this);
        
        disciplines.add(discipline);
    }
	
}
