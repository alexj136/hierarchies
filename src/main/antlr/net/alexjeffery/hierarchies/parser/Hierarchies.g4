grammar Hierarchies;

@parser::header {
    import net.alexjeffery.hierarchies.syntax.Declaration;
    import net.alexjeffery.hierarchies.syntax.Option;
    import net.alexjeffery.hierarchies.syntax.Field;
}

declaration returns [Declaration out]
    : IdentU '=' fields ';' { $out = new Declaration.Fixed($IdentU.text, $fields.out); }
    | IdentU '=' opts ';' { $out = new Declaration.Options($IdentU.text, $opts.out); }
    ;

opts returns [List<Option> out]
    : option '|' opts { $out = $opts.out; $out.add(0, $option.out); }
    | option { $out = new ArrayList<Option>(); $out.add($option.out); }
    ;

option returns [Option out]
    : '{' IdentU '}' fields { $out = new Option($IdentU.text, $fields.out); }
    ;

fields returns [List<Field> out]
    : field fields { $out = $fields.out; $out.add(0, $field.out); }
    | field { $out = new ArrayList<Field>(); $out.add($field.out); }
    ;

field returns [Field out]
    : IdentU { $out = new Field($IdentU.text); }
    | IdentU '*' { $out = new Field($IdentU.text, true); }
    | IdentU '?' { $out = new Field($IdentU.text, false, true); }
    | IdentU '*' '?' { $out = new Field($IdentU.text, true, true); }
    | IdentL ':' IdentU { $out = new Field($IdentL.text, $IdentU.text); }
    | IdentL ':' IdentU '*' { $out = new Field($IdentL.text, $IdentU.text, true); }
    | IdentL ':' IdentU '?' { $out = new Field($IdentL.text, $IdentU.text, false, true); }
    | IdentL ':' IdentU '*' '?' { $out = new Field($IdentL.text, $IdentU.text, true, true); }
    ;

Lower : ('a'..'z') ;
Upper : ('A'..'Z') ;
Alpha : Lower | Upper ;
IdentL : Lower Alpha * ;
IdentU : Alpha + ;
Whitespace : [ \t\r\n]+ -> skip ;
