package com.kalma.Patienten.Dossier.dto;

import java.util.ArrayList;
import java.util.List;

public class DossierDto {

    public Long id;

    public boolean dossierIsClosed;

    public String name;

    public List<Long> reportIds = new ArrayList<>();
}
