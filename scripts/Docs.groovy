//
// Docs command
//

includeTargets << grailsScript("Init")

PANDOC = "pandoc"        // The pandoc executable in your PATH
DIST = "target/docs"     // Target directory where docs are written


target(main: "Builds the Yana documentation") {
    ant.echo ( message: 'Building Yana documentation ....' )
    depends ( clean, expand, html )
}

target(clean: "Cleans the documentation target directory") {
    ant.delete( dir: DIST )
}

target(expand: "Expand templates" ) {
    ant.property (file: 'docs/version.properties')
    ant.echo ( message: 'Expanding templates for documentation version: ${version.number}' )
    ant.copy (file: 'docs/templates/title.txt.template',
              tofile: 'docs/title.txt', filtering: true) 
    ant.copy (file: "docs/templates/docs.css", 
              todir: "${DIST}/html", filtering: true )
}

target(figures: "Process the figures" ) {
   ant.echo(message: "Processing figures...")
   ant.mkdir( dir: "${DIST}/html/figures" )
   ant.copy(todir: "${DIST}/html/figures") {
     fileset(dir: "docs/figures") {
       include(name: "*.png")
     }
   }
}


target(html: "Generates the HTML pages" ) {
   ant.mkdir ( dir: "${DIST}/html" )
   depends( figures, userguide, apimanual, refpages )
   ant.echo ( message: 'Generating the index ...') 
   ant.copy ( file: "docs/en/index.md.template",
              tofile: "${DIST}/html/index.md", filtering:true )  
   // Concat the index files and generate index.html
   ant.exec ( executable: PANDOC ) {
     arg ( value: "-s" )
     arg ( value: "${DIST}/html/index.md" )
     arg ( value: "${DIST}/html/man5.index.md" )
     arg ( value: "--css=docs.css" )
     arg ( value: "--template=docs/templates/html.template" )
     arg ( value: "--include-before=docs/templates/before.html" )
     arg ( value: "--include-after=docs/templates/after.html" )
     arg ( value: "-o" )
     arg ( value: "${DIST}/html/index.html" )
   }
   ant.echo ( message: "Completed: ${DIST}/html/index.html")   
}

target(userguide: "Generates the HTML pages" ) {
   ant.echo ( message: 'Generating the User-Guide ...' )
   ant.exec ( executable: PANDOC ) {
     arg ( value: "--number-sections" )
     arg ( value: "--toc" )
     arg ( value: "-s" )
     arg ( value: "docs/title.txt" )
     arg ( value: "docs/en/guide/01-introduction.md" )
     arg ( value: "docs/en/guide/02-getting-started.md" )
     arg ( value: "docs/en/guide/03-beginning.md" )
     arg ( value: "--css=docs.css" )
     arg ( value: "--template=docs/templates/html.template" )
     arg ( value: "--include-before=docs/templates/before.html" )
     arg ( value: "--include-after=docs/templates/after.html" )
     arg ( value: "-o" )
     arg ( value: "${DIST}/html/User-Guide.html" )
   }
   ant.echo ( message: "Completed: ${DIST}/html/User-Guide.html")
}

target(apimanual: "Generate the API-Manual") {
   ant.echo ( message: 'Generating the API-Manual ...' )
   ant.exec ( executable: PANDOC ) {
     arg ( value: "--number-sections" )
     arg ( value: "--toc" )
     arg ( value: "-s" )
     arg ( value: "docs/title.txt" )
     arg ( value: "docs/en/api/01-chapter.md" )
     arg ( value: "--css=docs.css" )
     arg ( value: "--template=docs/templates/html.template" )
     arg ( value: "--include-before=docs/templates/before.html" )
     arg ( value: "--include-after=docs/templates/after.html" )
     arg ( value: "-o" )
     arg ( value: "${DIST}/html/API-Manual.html" )
   }
   ant.echo ( message: "Completed: ${DIST}/html/API-Manual.html" )
}

target(refpages: "Generate the reference pages") {
   ant.echo ( message: 'Generating reference pages ...' )
   ant.exec ( executable: PANDOC ) {
     arg ( value: "--number-sections" )
     arg ( value: "--toc" )
     arg ( value: "-s" )
     arg ( value: "docs/title.txt" )
     arg ( value: "docs/en/manpages/man5/node-v10.md" )
     arg ( value: "--css=docs.css" )
     arg ( value: "--template=docs/templates/html.template" )
     arg ( value: "--include-before=docs/templates/before.html" )
     arg ( value: "--include-after=docs/templates/after.html" )
     arg ( value: "-o" )
     arg ( value: "${DIST}/html/node-v10.html" )
   }
   ant.echo ( message: "Completed: ${DIST}/html/node-v10.html")
   
   ant.echo ( append:false,file: "${DIST}/html/man5.index.md", 
              message: "\n## File Formats\n" )
   new File( "docs/en/manpages/man5" ).eachFile{file->
     def basename = file.name.split('\\.md')[0]
     ant.echo ( append:true, file:"${DIST}/html/man5.index.md",
                message: "* [${basename}](${basename}.html)")
   }
}

setDefaultTarget(main)
