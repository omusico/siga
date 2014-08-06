package es.icarto.gvsig.commons.queries;

public interface QueriesWidget {

    /**
     * 
     * @return a 'trimmed' string represented the id of the query selected (or
     *         the first selected query if the widget allows multiple
     *         selection). Never returns null, it return an empty string "" in
     *         case there is no query selected
     */
    public String getQueryId();

    /**
     * 
     * @param id
     *            a string representing the id of the query
     * @return true if this query is selected in the widget. Is an
     *         implementation detail not defined by the spec returns true or
     *         false if more than one query can be selected, a one of the
     *         selected query is the searhed id
     */
    public boolean isQueryIdSelected(String id);

}
