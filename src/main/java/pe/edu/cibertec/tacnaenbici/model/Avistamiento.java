package pe.edu.cibertec.tacnaenbici.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "avistamiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avistamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate fecha;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalTime hora;

    private String departamento;

    private String provincia;

    private String distrito;

    private String direccion;

    private String referencia;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String foto;

    @Column(columnDefinition = "TEXT")
    private String foto2;

    @ManyToOne
    @JoinColumn(name = "bicicleta_id")
    private Bicicleta bicicleta;

    private Double latitud;
    private Double longitud;

}