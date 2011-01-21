#!/bin/bash

mvn site

site_dir=~/Documents/other/dev/payments4j-site/payments4j

cp -rf target/site $site_dir

for dir in payments4j-*; do 
  if [ -d "$dir" ] && [ -e "$dir/target/site" ]; then 
    from=$dir/target/site
    to=$site_dir/$dir
    echo "Copying $from to $to"
    cp -rf $from $to 
  fi 
done
