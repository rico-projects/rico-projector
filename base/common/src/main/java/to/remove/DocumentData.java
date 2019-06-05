package to.remove;

//import eu.medsea.mimeutil.MimeUtil;
import dev.rico.internal.projector.ForRemoval;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Base64;
import java.util.Objects;

@ForRemoval
public class DocumentData {
   static {
//      MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
   }

   private byte[] content;

   public static DocumentData from(String uuEncoded) {
      if (uuEncoded == null || uuEncoded.trim().isEmpty()) {
         throw new IllegalArgumentException("uuEncoded must not be null or empty");
      }
      DocumentData documentData = new DocumentData();
      documentData.content = Base64.getMimeDecoder().decode(uuEncoded);
      return documentData;
   }

   public static DocumentData from(File file) throws IOException {
      Objects.requireNonNull(file);
      DocumentData documentData = new DocumentData();
      documentData.content = IOUtils.toByteArray(new FileInputStream(file));
      return documentData;
   }

   public static DocumentData from(InputStream inputStream) {
      try {
         return from(IOUtils.toByteArray(inputStream));
      } catch (IOException e) {
         throw new IllegalArgumentException("Could not read from inputStream", e);
      }
   }

   public static DocumentData from(byte[] byteArray) {
      Objects.requireNonNull(byteArray);
      DocumentData documentData = new DocumentData();
      documentData.content = byteArray;
      return documentData;
   }

   public String asString() {
      Objects.requireNonNull(content);
      return Base64.getMimeEncoder().encodeToString(content);
   }

   public InputStream asInputStream() {
      Objects.requireNonNull(content);
      return new ByteArrayInputStream(content);
   }

   public Object getMimeType() {
      Objects.requireNonNull(content);
//      return MimeUtil.getMimeTypes(content).iterator().next();
      return "detection failed";
   }

   public byte[] getContent() {
      return content;
   }
}
