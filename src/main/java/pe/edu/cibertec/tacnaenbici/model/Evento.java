package pe.edu.cibertec.tacnaenbici.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String extracto;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private String imagen;

    private String autor;

    private String categoria;

    private LocalDateTime fechaPublicacion;
}