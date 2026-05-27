    /*
    * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
    * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
    */
    package cat.copernic.EasyTraza.entities;

    import cat.copernic.EasyTraza.enums.EstatLot;
    import jakarta.persistence.*;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    /**
     *
     * @author HAMZA
     */
    @Entity
    @IdClass(LotProveidorId.class)
    public class LotProveidor {

        // El identificador único del lote (suele ser alfanumérico, ej: L-4859)
        @Id
        private String idLot;

        @Id
        @Column(name = "proveidor_cif")
        private String proveidorCif;

        @Column(nullable = false)
        private Double quantitat;
        
        @Column(nullable = false)
        private Integer unitats; // NUEVO: Para guardar los sacos/cajas

        // Según el diagrama, tiene las fechas de caducidad, apertura y acabamiento
        private LocalDate dataCaducitat;
        private LocalDateTime dataObertura;
        private LocalDateTime dataAcabament;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private EstatLot estat = EstatLot.EN_ESTOC; // Por defecto entra EN ESTOC

        @ManyToOne
        @JoinColumn(name = "materia_primera_id", nullable = false)
        private MateriaPrimera materiaPrimera;

        @ManyToOne
        @JoinColumn(name = "albara_id", nullable = false)
        private AlbaraProveidor albara;

        @ManyToOne
        @JoinColumn(name = "nif_usuari_iniciador")
        private Usuari usuariIniciador;

        @ManyToOne
        @JoinColumn(name = "nif_usuari_finalitzador")
        private Usuari usuariFinalitzador;

        public LotProveidor() {}

        // --- Getters y Setters ---
        public String getIdLot() { return idLot; }
        public void setIdLot(String idLot) { this.idLot = idLot; }

        public String getProveidorCif() { return proveidorCif; }
        public void setProveidorCif(String proveidorCif) { this.proveidorCif = proveidorCif; }

        public Integer getUnitats() { return unitats; }
        public void setUnitats(Integer unitats) { this.unitats = unitats; }
        
        public Double getQuantitat() { return quantitat; }
        public void setQuantitat(Double quantitat) { this.quantitat = quantitat; }

        public LocalDate getDataCaducitat() { return dataCaducitat; }
        public void setDataCaducitat(LocalDate dataCaducitat) { this.dataCaducitat = dataCaducitat; }

        public LocalDateTime getDataObertura() { return dataObertura; }
        public void setDataObertura(LocalDateTime dataObertura) { this.dataObertura = dataObertura; }

        public LocalDateTime getDataAcabament() { return dataAcabament; }
        public void setDataAcabament(LocalDateTime dataAcabament) { this.dataAcabament = dataAcabament; }

        public EstatLot getEstat() { return estat; }
        public void setEstat(EstatLot estat) { this.estat = estat; }

        public MateriaPrimera getMateriaPrimera() { return materiaPrimera; }
        public void setMateriaPrimera(MateriaPrimera materiaPrimera) { this.materiaPrimera = materiaPrimera; }

        public AlbaraProveidor getAlbara() { return albara; }
        public void setAlbara(AlbaraProveidor albara) { this.albara = albara; }

        public Usuari getUsuariIniciador() { return usuariIniciador; }
        public void setUsuariIniciador(Usuari usuariIniciador) { this.usuariIniciador = usuariIniciador; }

        public Usuari getUsuariFinalitzador() { return usuariFinalitzador; }
        public void setUsuariFinalitzador(Usuari usuariFinalitzador) { this.usuariFinalitzador = usuariFinalitzador; }
    }