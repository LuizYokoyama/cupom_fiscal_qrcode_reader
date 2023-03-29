package com.ly.qrcode;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CupomEntity {

    @EqualsAndHashCode.Include
    private UUID id;

    private String nomeEmpresa;

    private String cnpj;

    private String endereco;

    private List<ItenEntity> itenEntityList;

    private int qtdTotalItens;

    private Float valorPagar;

    private Float cartaoCredito;

    private Float cartaoDebito;

    private Float dinheiro;

    private Float pix;

    private Float troco;

    private Float tributos;

    private String emissao;


}
