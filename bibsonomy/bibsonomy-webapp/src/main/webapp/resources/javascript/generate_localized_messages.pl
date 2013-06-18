#
# This script parses messages*.properties files and generates
# corresponding javascript files with an array containing the 
# same key > string association.
#
# Changes:
# 2011-06-27, dzo
# - included javascript files in subdirectories
# 2011-05-06, dbe
# - included puma javascript files
# 2011-04-11, rja
# - script runs now over all JavaScript files and extracts the 
#   message key patterns that are actually used 
# dbe, 20071204
# - initial version
#

#!/usr/bin/perl
#
#
#  BibSonomy-Webapp - The webapplication for Bibsonomy.
#
#  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
#                            University of Kassel, Germany
#                            http://www.kde.cs.uni-kassel.de/
#
#  This program is free software; you can redistribute it and/or
#  modify it under the terms of the GNU General Public License
#  as published by the Free Software Foundation; either version 2
#  of the License, or (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
#

use strict;
use Encode;
use File::Find;

# Collect all occurrences of calls to the "getString()" method that we
# use for localization in JavaScript files - from BibSonomy and Puma

my @directories_to_search = ('.', '../../resources_puma/javascript');
my @jsFiles = ();
find(sub { push(@jsFiles, $File::Find::name) if /\.js$/ }, @directories_to_search);

# patterns of the message keys found in JavaScript files
my %keyPatterns = ();

print STDERR "\nINFO: extracting message keys from JavaScript files " . join (", ", @jsFiles) . "\n\n";

foreach my $file (@jsFiles) {
    open (JS, "<$file") or die "could not open $file\n";
    while (<JS>) {
	# extract all calls to the "getString" method
	if (/getString\((.+?)\)/) {
	    # method arguments
	    my $arg = $1;
	    # check the method arguments
	    if ($arg =~ m/^["']([0-9\.\_a-zA-Z]+)["']$/) {
		# found string literal -> add as literal pattern
		$keyPatterns{quotemeta($1)} = 1;
	    } else {
		# Something more complicated - try if we have at least
		# a string literal at the beginning, e.g., "navi." + var
		if ($arg =~ m/^["']([0-9\.\_a-zA-Z]+)["']\s*\+\s*(.+)$/) {
		    # found - append "catch all" regex ".*"
		    my $keyPattern = quotemeta($1) . ".*";
		    print STDERR "WARN: Converting '$arg' to '$keyPattern'.\n";
		    $keyPatterns{$keyPattern} = 1;
		} else {
		    print STDERR "ERROR: Could not extract message key from \"getString($1)\" found in $file.\n";
		}
	    }
	}
    }
    close (JS);
}
# print keys
print STDERR "\nINFO: key patterns found: " . join(", ", sort keys %keyPatterns) . "\n\n";



# directory with messages.properties, messages_de.properties, ...
my @files = <../../../resources/messages*.properties>;

# parse properties file and generate javascript file
foreach my $file (@files) {
    print STDERR "INFO: processing " . $file . " ... ";
    $file =~ /.*messages[\_]?([^\.]+)\.properties/;
    my $locale = ($1 ? $1 : 'en');
    
    # open javascript localized strings file
    open(JS, "> ./localized_strings_$locale.js");
    print STDERR "writing localized_strings_$locale.js ... ";
    print JS "// use the getString() method for localization!\n";
    print JS "var LocalizedStrings = {";
    
    open(M, "< $file");
    
    my $pairs = "";
    
    # parse properties file and write contents into javascript array
    LINE: foreach my $line (<M>) {
	chomp($line);

	# skip comments
	next LINE if ($line =~ m/\s*#/);
	#
	# messages_de.properties is in latin1 (because of a strange Java/JSTL bug)
	# but we want UTF-8 for the JavaScript file ... so we convert
	#
	if ($locale eq "de") {
	    $line = encode("utf-8", decode("iso-8859-1", $line));
	}
	
	my ($key, $value) = split("=", $line);
	# remove leading/trailing whitespace
	$key = trim($key);
	$value = trim($value);

	# skip keys not used in JavaScript files
	next LINE if (!keyFound($key));

	# escape backslashes 
	# rja, 2009-09-28: disabled, because we need line breaks (\n)
	# $value =~ s/\\/\\\\/g;
	# but keep backslashes indicating unicode chars
	# $value =~ s/\\\\u/\\u/g;		
	# escape quotation marks & backslashes
	$value =~ s/\"/\\\"/g;
	if (length($key) > 0 and length($value) > 0 and not (substr($key,0,1) eq '#')) {
	    $pairs .= '"' . $key . '":"' . $value . '",';
	}
    }
    
    # remove last comma
    $pairs = substr($pairs, 0, length($pairs) - 1 );
    
    print JS $pairs;
    
    print JS "};";
    print STDERR "done.\n";
    close JS;
    close M;
}

print "\nINFO: checking for possibly missing keys (if unsure, look at the comments in this script)\n";

#
# check if all keys appeared
# 
# NOTE: if a key did not appear, it's probably nevertheless in the
# file, because a wildcard matched. E.g., 
# navi\.search could be reported as missing, although it is contained, 
# since it was matched by "navi\..*"
#
foreach my $key (sort keys %keyPatterns) {
    if ($keyPatterns{$key} < 2) {
	print STDERR "WARN: no key found for pattern $key.\n";
    }
}


# checks if the given key is found in %keyPatterns, i.e., in a
# JavaScript file
sub keyFound {
    my $s = shift;
    # We do a "reverse sort" such that the wildcard patterns come at
    # the end. This helps to minimize the number of erronously
    # reported "missing keys".
    foreach my $key (reverse sort keys %keyPatterns) {
	if ($s =~ m/^$key$/) {
	    # increase counter - such that we can later on check if
	    # all keys appeared
	    $keyPatterns{$key}++;
	    return 1;
	}
    }
    return 0;
}

sub trim($){
    my $string = shift;
    $string =~ s/^\s+//;
    $string =~ s/\s+$//;
    return $string;
}
