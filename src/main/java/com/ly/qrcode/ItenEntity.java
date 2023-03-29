package com.ly.qrcode;

import lombok.*;
import org.checkerframework.checker.signature.qual.Identifier;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItenEntity {

    @EqualsAndHashCode.Include
    private UUID id;

    private String nomeItem;

    private int qtd;

    private String unidade;

    private float valorUnit;

    private float valorTotalItem;

}
