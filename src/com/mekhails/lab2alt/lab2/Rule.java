package com.mekhails.lab2alt.lab2;

import ru.spbstu.pipeline.RC;

@FunctionalInterface
interface Rule
{
    public RC apply(Object obj);
}
