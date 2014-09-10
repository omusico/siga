package es.icarto.gvsig.commons.queries;

import java.util.Collection;
import java.util.List;

public interface QueryFiltersI {

    public Collection<Field> getLocation();

    public List<Field> getFields();

}
