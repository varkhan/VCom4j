
################################################################################
###
### Xon
###

package Xon;

use strict;
use warnings;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK %EXPORT_TAGS);
$Xon::VERSION = '0.10';

require Exporter;

@Xon::ISA = qw(Exporter);
@Xon::EXPORT = qw(write read equals);
@Xon::EXPORT_OK = qw(
    null true false
    write  writeNull  writeBoolean  writeNumber  writeString  writeArray  writeCollec  writeObject
    read   readNull   readBoolean   readNumber   readString   readArray   readCollec   readObject
    equals isNull     isBoolean     isNumber
);
%Xon::EXPORT_TAGS = (
        'CONST'       => [qw(null true false)],
        'WRITE'       => [qw(write        writeNull    writeBoolean  writeNumber   writeString   writeArray  writeCollec  writeObject)],
        'READ'        => [qw(read         readNull     readBoolean   readNumber    readString    readArray   readCollec   readObject)],
        'EQUALS'      => [qw(equals       equalsNull   equalsBoolean equalsNumber  equalsString  equalsArray equalsCollec equalsObject)],
        'ATOMS'       => [qw(writeNull    writeBoolean writeNumber   writeString   readNull      readBoolean readNumber   readString)],
        'LISTS'       => [qw(writeArray   writeCollec  readArray     readCollec)],
        'OBJECT'      => [qw(writeObject  readObject   equalsObject)],
        'BASE'        => \@Xon::EXPORT,
        'ALL'         => \@Xon::EXPORT_OK
);


sub null  { undef; }

sub isNull($) {
    my $v = shift;
    return !defined $v;
}

eval q{
    package Xon::Boolean;
    $Xon::Boolean::false = do { bless \(my $b=0), 'Xon::Boolean' };
    $Xon::Boolean::true  = do { bless \(my $b=1), 'Xon::Boolean' };
    use overload (
        "0+"     => sub { ${$_[0]} },
        "++"     => sub { $_[0] = ${$_[0]} + 1 },
        "--"     => sub { $_[0] = ${$_[0]} - 1 },
        '""'     => sub { ${$_[0]} },
        fallback => 1,
    );
};

sub false { $Xon::Boolean::false }
sub true  { $Xon::Boolean::true  }

sub isBoolean {
    my $v = shift;
    return defined($v) && ref($v) eq 'Xon::Boolean';
}

sub asBoolean($v) {
    my $v = shift;
    return 0+${$v[0]};
}



my $NUM_RE = qr{
  (
    [-+]?
    (?: 0 | [1-9][0-9]* )
    (?: \. [0-9]+ )?
    (?: [eE] [-+]? [0-9]+ )?
  )
}xms;


sub isNumber($) {
    my $v = shift;
    return $v =~ m/^${NUM_RE}$/o;
}


sub equals($$) {
    my $v1 = shift;
    my $v2 = shift;
    if($v1 eq $v2) { return 1; }
    elsif(ref($v1) ne ref($v2)) { return 0; }
    elsif(isNull($v1) && isNull($v2)) { return 1; }
    elsif(isNumber($v1) && isNumber($v2)) {
        return $v1 == $v2;
    }
    elsif(isBoolean($v1) && isBoolean($v2)) {
        return ${$v1[0]} == ${$v2[0]};
    }
    elsif(ref($v1) eq 'ARRAY' && ref($v2) eq 'ARRAY') {
        my @v1 = @$v1;
        my @v2 = @$v2;
        for my $i1 (keys @v1) {
            if(!equals($v1[$i1],$v2[$i1])) { return 0; }
        }
        for my $i2 (keys @v2) {
            if(!equals($v1[$i2],$v2[$i2])) { return 0; }
        }
        return 1;
    }
    elsif(ref($v1) eq 'HASH' && ref($v2) eq 'HASH') {
        my %v1 = %$v1;
        my %v2 = %$v2;
        for my $k1 (keys %v1) {
            if(!equals($v1{$k1},$v2{$k1})) { return 0; }
        }
        for my $k2 (keys %v2) {
            if(!equals($v1{$k2},$v2{$k2})) { return 0; }
        }
        return 1;
    }
    elsif(ref($v1) eq 'SCALAR' && ref($v2) eq 'SCALAR') {
        if($$v1 eq $$v2) { return 1; }
        elsif(ref($$v1) eq 'ARRAY' && ref($$v2) eq 'ARRAY') {
            my @v1 = @$$v1;
            my @v2 = @$$v2;
            for my $i1 (keys @v1) {
                if(!equals($v1[$i1],$v2[$i1])) { return 0; }
            }
            for my $i2 (keys @v2) {
                if(!equals($v1[$i2],$v2[$i2])) { return 0; }
            }
            return 1;
        }
    }
    return 0;
}


sub write($) {
    my $v = shift;
    if(isNull($v)) { return 'null'; }
    elsif(isBoolean($v)) { return ${$v[0]}?'true':'false'; }
    elsif(ref($v) eq 'ARRAY') {
        return '['.join(',',map {write($_)} @$v).']';
    }
    elsif(ref($v) eq 'HASH') {
        return '{'.join(',',map {writeField($_).(isNull($v->{$_})?'':(':'.write($v->{$_})))} keys(%$v)).'}';
    }
    elsif(ref($v) eq 'SCALAR') {
        if(ref($$v) eq 'ARRAY') {
            return '('.join(',',map {write($_)} @$$v).')';
        }
        else {
            return writeString($$v);
        }
    }
    else {
        return writeNumber($v);
    }
}

my $FLD_RE = qr/[_a-zA-Z][_a-zA-Z0-9]*/;

sub writeField($) {
    my $v = shift;
    if($v=~m/^$FLD_RE$/) { return $v; }
    return writeString($v);
}

sub writeString($) {
    my $v = shift;
    $v =~ s/\\/\\\\/;
    $v =~ s/"/\\"/;
    return '"'.$v.'"';
}

sub writeNumber($) {
    my $v = shift;
    return ""+(0+$v);
}


1;

#my $XON_RE = qr{
#
#(?&VALUE) (?{ $_ = $^R->[1] })
#
#(?(DEFINE)
#
#(?<OBJECT>
#  (?{ [$^R, {}] })
#  \{
#    (?: (?&KV) # [[$^R, {}], $k, $v]
#      (?{ # warn Dumper { obj1 => $^R };
#     [$^R->[0][0], {$^R->[1] => $^R->[2]}] })
#      (?: , (?&KV) # [[$^R, {...}], $k, $v]
#        (?{ # warn Dumper { obj2 => $^R };
#       [$^R->[0][0], {%{$^R->[0][1]}, $^R->[1] => $^R->[2]}] })
#      )*
#    )?
#  \}
#)
#
#(?<KV>
#  \s* (?&STRING) \s* # [$^R, "string"]
#  (:? : (?&VALUE) ) ?# [[$^R, "string"], $value]
#  (?{ # warn Dumper { kv => $^R };
#     [$^R->[0][0], $^R->[0][1], $^R->[1]] })
#)
#
#(?<VECTOR>
#  (?{ [$^R, []] })
#  \[
#    (?: (?&VALUE) (?{ [$^R->[0][0], [$^R->[1]]] })
#      (?: , (?&VALUE) (?{ # warn Dumper { atwo => $^R };
#             [$^R->[0][0], [@{$^R->[0][1]}, $^R->[1]]] })
#      )*
#    )?
#  \]
#)
#
#(?<COLLEC>
#  (?{ [$^R, []] })
#  \(
#    (?: (?&VALUE) (?{ [$^R->[0][0], [$^R->[1]]] })
#      (?: , (?&VALUE) (?{ # warn Dumper { atwo => $^R };
#             [$^R->[0][0], [@{$^R->[0][1]}, $^R->[1]]] })
#      )*
#    )?
#  \)
#)
#
#(?<VALUE>
#  \s*
#  ( (?&STRING) | (?&NUMBER) | (?&OBJECT) | (?&VECTOR) | (?&COLLEC)
#  |
#    true (?{ [$^R, \1] })
#  |
#    false (?{ [$^R, \0] })
#  |
#    null (?{ [$^R, undef] })
#  )
#  \s*
#)
#
#(?<STRING>
#  (
#    "
#    (?:
#      [^\\"]+
#    |
#      \\ ["\\/bfnrt]
##    |
##      \\ u [0-9a-fA-f]{4}
#    )*
#    "
#  )
#
#  (?{ [$^R, eval $^N] })
#)
#
#(?<NUMBER>
#  (
#    -?
#    (?: 0 | [1-9]\d* )
#    (?: \. \d+ )?
#    (?: [eE] [-+]? \d+ )?
#  )
#
#  (?{ [$^R, eval $^N] })
#)
#
#) }xms;
