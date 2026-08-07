package pe.edu.cibertec.tacnaenbici.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name = "bicicleta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(name = "numero_serie", unique = true, length = 100)
    private String numeroSerie;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "caracteristicas_particulares", columnDefinition = "TEXT")
    private String caracteristicasParticulares;

    @Column(columnDefinition = "TEXT")
    private String foto;

    @Column(columnDefinition = "TEXT")
    private String foto2;

    @Column(columnDefinition = "TEXT")
    private String foto3;

    @Column(columnDefinition = "TEXT")
    private String foto4;

    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate fechaRobo;

    private String departamento;

    private String provincia;

    private String distrito;

    private String lugarRobo;

    @Column(columnDefinition = "TEXT")
    private String descripcionRobo;

    private Boolean denunciaPolicial;

    private String estado;

    @Column(length = 20)
    private String estadoAprobacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private Double latitud;
    private Double longitud;
}