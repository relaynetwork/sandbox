function add1( val, prog ) {
  prog(val+1);
}

add1(3, function(val) { println("val is: " + val); });
