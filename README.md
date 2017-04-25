
#Ant template for modular flex project
[TOC]
##Outline
```
root
  ├─bin                         #
  ├─ext
  │  ├─ant_contrib
  │  ├─ant_flex
  │  ├─java_jar
  │  ├─playerglobal
  │  │  └─12.0
  │  ├─template
  │  │  ├─swc_project           #Swc project template
  │  │  │  ├─.lib               #Internal static share libaray for project
  │  │  │  ├─.rsl               #Internal runtime share libaray for project
  │  │  │  ├─.settings
  │  │  │  ├─bin
  │  │  │  ├─lib                #External static share libaray for project
  │  │  │  ├─rsl                #External runtime share libaray for project
  │  │  │  └─src
  │  │  └─swf_project           #Swf project template
  │  │      ├─.lib              #Internal static share libaray for project
  │  │      ├─.rsl              #Internal runtime share libaray for project
  │  │      ├─.settings
  │  │      ├─bin
  │  │      ├─lib               #External static share libaray for project
  │  │      ├─rsl               #External runtime share libaray for project
  │  │      └─src
  │  ├─util_order
  │  │  ├─dst
  │  │  ├─lib
  │  │  └─src
  │  └─util_setting
  │      ├─dst
  │      └─src
  ├─lib                         #Global static share libaray
  ├─log                         #
  ├─ref                         #
  ├─rsl                         #Global runtime Share libaray
  └─src
```
##To SVN User, before the first commit you need to:
1. add the follow ignore list to the root dir:

   > ```
   > bin
   > log
   > ref
   > config4*.properties
   > flex_config_*.xml
   > ```

2. edit the **config.properties.base** and set:

   > ```
   > config.svn.is_use=true
   > ```

## Good Luck!!

