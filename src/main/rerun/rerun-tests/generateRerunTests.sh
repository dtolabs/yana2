#!/bin/bash

importFile=$1
rerunCommand=$(dirname $0)/../../../../../rerun/rerun	
rerunBaseCommand="${rerunCommand} -M $(dirname $0)/../modules"
benchFileOutput=/tmp/$(basename $0).$$.gv.tmp
rerunFileOutput=/tmp/$(basename $0).$$.rr.tmp
END_OF_TEST_MARKER="==========END_OF_TEST=========="

[ ! -r $importFile ] \
  && echo "usage: $0 <xml-import-file>" 1>&2 \
  && exit 1
[ ! -x ${rerunCommand} ] \
  && echo "ERROR: please ensure 'rerun' is located adjacent to your 'yana2' clone" 1>&2 \
  && exit 2
which groovy > /dev/null \
  || echo "ERROR: please ensure 'groovy' is in your PATH" 1>&2 \
  || exit 3

importFile=$(pushd $(dirname $1) > /dev/null; pwd ; popd > /dev/null)/$(basename $1)

groovy -cp ../../../grails-app/domain \
       $(dirname $0)/MakeRerunTests.groovy \
       "${importFile}" \
       $(dirname $0)/../../../../docs/yana.xsd \
       "${END_OF_TEST_MARKER}" \
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
          eval $rerunBaseCommand $rerunCommand \
          | sed '/^$/d' > ${rerunFileOutput}.${testName}
          diff -w ${benchFileOutput}.${testName} \
                  ${rerunFileOutput}.${testName} \
                > ${benchFileOutput}.${testName}.diff
          if [ $? -ne 0 ] ; then
            echo ================== TEST FAILED: $testName ================== 
            echo $rerunBaseCommand $rerunCommand
            cat ${benchFileOutput}.${testName}.diff
          else
            echo ================== TEST PASSED: $testName ================== 
            echo $rerunBaseCommand $rerunCommand
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
