<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="grails.plugin.searchable.internal.lucene.LuceneUtils" %>
<%@ page import="grails.plugin.searchable.internal.util.StringQueryUtils" %>
<%@ page import="com.dtosolutions.Node" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <title><g:if test="${params.q && params.q?.trim() != ''}">${params.q} - </g:if>Grails Searchable Plugin</title>
    <style type="text/css">
      .result {
        margin-bottom: 1em;
      }

      .result .displayLink {
        color: green;
      }

      .result .name {
        font-size: larger;
      }

      .paging a.step {
        padding: 0 .3em;
      }

      .paging span.currentStep {
          font-weight: bold;
      }

    </style>
    <script type="text/javascript">
        var focusQueryInput = function() {
            document.getElementById("q").focus();
        }
    </script>
  </head>
  <body onload="focusQueryInput();">
</br>
  <div id="main">
    <g:set var="haveQuery" value="${params.q?.trim()}" />
    <g:set var="haveResults" value="${searchResult?.results}" />
    <div class="title">
      <span>
        <g:if test="${haveQuery && haveResults}">
          Showing <strong>${searchResult.offset + 1}</strong> - <strong>${searchResult.results.size() + searchResult.offset}</strong> of <strong>${searchResult.total}</strong>
          results for <strong>${params.q}</strong></br></br>
        </g:if>
        <g:else>
        &nbsp;
        </g:else>
      </span>
    </div>

    <g:if test="${haveQuery && !haveResults && !parseException}">
      <p>Nothing matched your query - <strong>${params.q}</strong></p>
      <g:if test="${!searchResult?.suggestedQuery}">
        <p>Suggestions:</p>
        <ul>
          <li>Try a suggested query: <g:link controller="searchable" action="index" params="[q: params.q, suggestQuery: true]">Search again with the <strong>suggestQuery</strong> option</g:link><br />
            <em>Note: Suggestions are only available when classes are mapped with <strong>spellCheck</strong> options, either at the class or property level.<br />
		The simplest way to do this is add <strong>spellCheck "include"</strong> to the domain class searchable mapping closure.<br />
                See the plugin/Compass documentation Mapping sections for details.</em>
          </li>
        </ul>
      </g:if>
    </g:if>

    <g:if test="${searchResult?.suggestedQuery}">
      <p>Did you mean <g:link controller="searchable" action="index" params="[q: searchResult.suggestedQuery]">${StringQueryUtils.highlightTermDiffs(params.q.trim(), searchResult.suggestedQuery)}</g:link>?</p>
    </g:if>

    <g:if test="${parseException}">
      <p>Your query - <strong>${params.q}</strong> - is not valid.</p>
      <p>Suggestions:</p>
      <ul>
        <li>Fix the query: see <a href="http://lucene.apache.org/java/docs/queryparsersyntax.html">Lucene query syntax</a> for examples</li>
        <g:if test="${LuceneUtils.queryHasSpecialCharacters(params.q)}">
          <li>Remove special characters like <strong>" - [ ]</strong>, before searching, eg, <em><strong>${LuceneUtils.cleanQuery(params.q)}</strong></em><br />
              <em>Use the Searchable Plugin's <strong>LuceneUtils#cleanQuery</strong> helper method for this: <g:link controller="searchable" action="index" params="[q: LuceneUtils.cleanQuery(params.q)]">Search again with special characters removed</g:link></em>
          </li>
          <li>Escape special characters like <strong>" - [ ]</strong> with <strong>\</strong>, eg, <em><strong>${LuceneUtils.escapeQuery(params.q)}</strong></em><br />
              <em>Use the Searchable Plugin's <strong>LuceneUtils#escapeQuery</strong> helper method for this: <g:link controller="searchable" action="index" params="[q: LuceneUtils.escapeQuery(params.q)]">Search again with special characters escaped</g:link></em><br />
              <em>Or use the Searchable Plugin's <strong>escape</strong> option: <g:link controller="searchable" action="index" params="[q: params.q, escape: true]">Search again with the <strong>escape</strong> option enabled</g:link></em>
          </li>
        </g:if>
      </ul>
    </g:if>

	<g:if test="${searchResult?.results}">
		<div id="list-node" class="content scaffold-list" role="main">
			<table>
				<thead>
					<tr>
				
						<g:sortableColumn property="name" title="${message(code: 'node.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="nodetype" title="${message(code: 'node.nodetype.name.label', default: 'Nodetype')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'node.status.label', default: 'Status')}" />
						
						<g:sortableColumn property="tags" title="${message(code: 'node.tags.label', default: 'Tags')}" />
						
						<g:sortableColumn property="description" title="${message(code: 'node.description.label', default: 'Description')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each var="result" in="${searchResult.results}" status="index">
					<g:set var="nodeInstance" value="${Node.get(result.id.toLong())}"/>
					<g:set var="name" value="${ClassUtils.getShortName(result.getClass())}" />
            		<g:set var="className" value="${ClassUtils.getShortName(result.getClass())}" />
            		<g:set var="link" value="${createLink(controller: className[0].toLowerCase() + className[1..-1], action: 'show', id: result.id)}" />
					<tr class="${(index % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link controller="node" action="show" id="${nodeInstance.id}">${fieldValue(bean: nodeInstance, field: "name")}</g:link></td>
					
						<td><g:link controller="nodeType" action="show" id="${nodeInstance.nodetype.id}">${nodeInstance.nodetype.name}</g:link></td>
					
						<td>${fieldValue(bean: nodeInstance, field: "status")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "tags")}</td>
					
						<td>${fieldValue(bean: nodeInstance, field: "description")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${Node.count()}" />
			</div>
		</div>
	</g:if>
    <g:else>
    <div class="message" role="status">Invalid search: Please try again.</div>
    </g:else>
  </div>
  </body>
</html>
