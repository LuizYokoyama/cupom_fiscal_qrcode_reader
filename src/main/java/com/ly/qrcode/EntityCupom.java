package com.ly.qrcode;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EntityCupom {

    @EqualsAndHashCode.Include
    private UUID id;




}
