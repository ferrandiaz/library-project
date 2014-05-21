package edu.upc.eetac.dsa.ferrandiaz.library.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
 
import edu.upc.eetac.dsa.ferrandiaz.library.api.model.LibraryRootAPI;
 
@Path("/")
public class LibraryRootAPIResource {
	@GET
	public LibraryRootAPI getRootAPI() {
		LibraryRootAPI api = new LibraryRootAPI();
		return api;
	}
}