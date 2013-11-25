#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>

#define LINE_TERM '\n'
#define BSIZE_CHUNK 1024

char* read_line (char** buff, size_t *bsize, FILE* fp) {
  long bytes_read = 0, offset = 0, pos = 0, file_start;
  char* remalloc, *lterm = NULL;


  // try to read bsize byets into the beginning of the buffer
  // if we read 0, we're all done, we hit EOF
  // if we read any, check for a newline
  //   if we found one, rewind the file pointer, null out the newline and return the line
  // if we read less bytes than we wanted, we're done b/c we hit EOF
  // if we read all the bytes we wanted, we didn't get a full-line, realloc and
  // read again (recurse?)

  file_start = ftell(fp);
  fprintf(stderr, "read_line: Reading a line! bsize=%ld file_start=%ld\n", *bsize, file_start);
  *buff[0] = '\0';

  bytes_read = fread((*buff+offset), sizeof(char), *bsize, fp);
  if (bytes_read == 0) {
    return *buff;
  }
  offset += bytes_read;
  fprintf(stderr, "read_line: buff is: '%s'\n", *buff);

  while (NULL == (lterm = strchr(*buff, LINE_TERM))) {
    fprintf(stderr, "read_line: newline not found bsize: %ld\n", *bsize);
    *bsize += BSIZE_CHUNK;
    fprintf(stderr, "read_line: newline not found\n");
    remalloc = realloc(*buff, *bsize);
    if(!remalloc) {
      fprintf(stderr, "read_line: Error increasing buffer memory: %s\n", strerror(errno));
      exit(1);
    }
    fprintf(stderr, "read_line: Reallocated buff to %ld\n", *bsize);

    *buff = remalloc;

    bytes_read = fread((*buff+offset), sizeof(char), *bsize, fp);
    if (bytes_read == 0) {
      return *buff;
    }

    offset += bytes_read;
  }

  if (lterm == NULL) {
    fprintf(stderr, "No line term found in buff\n");
    return *buff;
  }

  // we found a newline at pos
  *lterm = '\0';
  pos = lterm - *buff;
  fprintf(stdout, "read_line: We read %ld bytes found the newline at %ld\n", bytes_read, pos);
  if (fseek(fp, file_start + pos, SEEK_SET)) {
    fprintf(stderr, "read_line: Error rewinding the file pointer: %s\n", strerror(errno));
    exit(1);
  }

  return *buff;
}

int main (int argc, char **argv) {
  FILE *fp   = NULL;
  char* buff = NULL;
  size_t bsize = 0;

  if (argc < 2) {
    fprintf(stderr, "Error: you must supply a file to open. %s <<file>>\n", argv[0]);
    exit(1);
  }

  printf("ok, read the file here %s\n", argv[1]);
  fp = fopen(argv[1], "r");
  fprintf(stderr, "file is open: %p\n", fp);

  if (!fp) {
    fprintf(stderr, "Error opening input file: %s : %s\n", argv[1], strerror(errno));
    exit(1);
  }

  fprintf(stderr, "file is open: %p\n", fp);

  buff = malloc(BSIZE_CHUNK*sizeof(char));
  if (!buff) {
    perror("Error allocating memory.");
    exit(errno);
  }
  bsize = BSIZE_CHUNK;
  buff[0] = '\0';

  fprintf(stderr, "about to call read_line(%p, %p, %p) buff='%s'\n", &buff, &bsize, fp, buff);
  while (buff = read_line(&buff, &bsize, fp)) {
    if (0 == strlen(buff)) {
      fprintf(stdout, "EOF");
      break;
    }
    fprintf(stdout, "LINE: '%s'\n", buff);
  }

  fclose(fp);
  free(buff);

  return 0;
}
