package es.icarto.gvsig.extgex.queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.commons.queries.QueryFiltersI;

public class QueryFilters implements QueryFiltersI {

    private final List<Field> location;
    private List<Field> fields;

    public QueryFilters(String[] filters) {

	location = new ArrayList<Field>();
	Field tramo = new Field("", "Tramo");
	tramo.setValue(filters[0] == null ? "" : filters[0]);
	Field uc = new Field("", "UC");
	uc.setValue(filters[1] == null ? "" : filters[1]);
	Field ayuntamiento = new Field("", "Ayuntamiento");
	ayuntamiento.setValue(filters[2] == null ? "" : filters[2]);

	Field parroquia = new Field("", "Parroquia/Subtramo");
	parroquia.setValue(filters[3] == null ? "" : filters[3]);

	location.add(tramo);
	location.add(uc);
	location.add(ayuntamiento);
	location.add(parroquia);

    }

    @Override
    public Collection<Field> getLocation() {
	return location;
    }

    @Override
    public List<Field> getFields() {
	return fields;
    }

    public void setQueryType(String string) {
    }

    public void setFields(List<Field> fields) {
	this.fields = fields;
    }

}
