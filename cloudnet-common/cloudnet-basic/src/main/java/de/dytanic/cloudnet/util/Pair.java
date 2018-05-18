package de.dytanic.cloudnet.util;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class Pair<T, TT> {

    private T first;

    private TT second;

}