#!/bin/bash

END_OF_TEST_MARKER="==========END_OF_TEST=========="

cd $(dirname $0)

importFile=$1

rerunBaseCommand="../../../../../rerun/rerun -M ../modules"

benchFileOutput=/tmp/$(basename $0).$$.gv.tmp
rerunFileOutput=/tmp/$(basename $0).$$.rr.tmp

groovy -cp ../../../../grails-app/domain ./MakeRerunTests.groovy \
       $1 ../../../../docs/yana.xsd \
       ${END_OF_TEST_MARKER} \
| while read line ; do
    case $line in
      TEST:*)
          testName=${line/TEST:/}
          cat /dev/null > ${benchFileOutput}.${testName}
          cat /dev/null > ${rerunFileOutput}.${testName}
          ;;
      RERUN:*)
          rerunCommand=${line/RERUN:/}
          ;;
      ${END_OF_TEST_MARKER})
          echo ================== TEST: $testName ================== 
          eval $rerunBaseCommand $rerunCommand \
          | sed '/^$/d' > ${rerunFileOutput}.${testName}
          sort ${benchFileOutput}.${testName} > ${benchFileOutput}.${testName}.s
          mv ${benchFileOutput}.${testName}.s ${benchFileOutput}.${testName}
          sort ${rerunFileOutput}.${testName} > ${rerunFileOutput}.${testName}.s
          mv ${rerunFileOutput}.${testName}.s ${rerunFileOutput}.${testName}
          diff -w ${rerunFileOutput}.${testName} ${benchFileOutput}.${testName} \
                > ${benchFileOutput}.${testName}.diff
          if [ $? -ne 0 ] ; then
            sed -i '/^$/d' ${benchFileOutput}.${testName}.diff
            echo FAILED: $rerunCommand
            cat ${benchFileOutput}.${testName}.diff
          fi 
          rm -f ${rerunFileOutput}.${testName} \
                ${benchFileOutput}.${testName} \
                ${benchFileOutput}.${testName}.diff
          ;;
      *)
          echo $line >> ${benchFileOutput}.${testName}
          ;;
    esac
done
