/** Calculates the byte length of a string encoded as UTF8. */
function utf8Length(string) {
  var len = 0,
      c = 0;
  for (var i = 0; i < string.length; ++i) {
      c = string.charCodeAt(i);
      if (c < 128)
          len += 1;
      else if (c < 2048)
          len += 2;
      else if (
        (c                        & 0xFC00) === 0xD800 &&
        (string.charCodeAt(i + 1) & 0xFC00) === 0xDC00
      ) {
          ++i;
          len += 4;
      } else
          len += 3;
  }
  return len;
}

/** Converts a string to an array of UTF8 bytes. */
function utf8Array(string) {
  var offset = 0,
      c1, c2;
  var buffer = new Array(utf8Length(string));
  for (var i = 0, k = string.length; i < k; ++i) {
      c1 = string.charCodeAt(i);
      if (c1 < 128) {
          buffer[offset++] = c1;
      } else if (c1 < 2048) {
          buffer[offset++] = c1 >> 6       | 192;
          buffer[offset++] = c1       & 63 | 128;
      } else if (
        ( c1                             & 0xFC00) === 0xD800 &&
        ((c2 = string.charCodeAt(i + 1)) & 0xFC00) === 0xDC00
      ) {
          c1 = 0x10000 + ((c1 & 0x03FF) << 10) + (c2 & 0x03FF);
          ++i;
          buffer[offset++] = c1 >> 18      | 240;
          buffer[offset++] = c1 >> 12 & 63 | 128;
          buffer[offset++] = c1 >> 6  & 63 | 128;
          buffer[offset++] = c1       & 63 | 128;
      } else {
          buffer[offset++] = c1 >> 12      | 224;
          buffer[offset++] = c1 >> 6  & 63 | 128;
          buffer[offset++] = c1       & 63 | 128;
      }
  }
  return buffer;
}
