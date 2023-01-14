package id.kawahedukasi.request;

import javax.ws.rs.FormParam;

public class FileMulipartRequest {

   @FormParam("file")
   public byte[] file;
}
