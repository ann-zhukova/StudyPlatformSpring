<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:apply-templates select="topics|topic|tasks|task"/>
    </xsl:template>

    <xsl:template match="topics">
        <html>
        <head>
            <title>Topics</title>
            <meta charset="UTF-8"/>
            <style>
                body { font-family: Arial, sans-serif; margin: 1rem 2rem; color: #222; }
                h1 { margin: 0 0 1rem 0; font-size: 1.6rem; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; }
                td { vertical-align: top; }
                th { background: #f3f3f3; text-align: left; }
                tbody tr:nth-child(even) { background: #fafafa; }
                tbody tr:hover { background: #eef6ff; }
                a { text-decoration: none; color: #0b63b6; }
                a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>
        <h1>Topics</h1>
        <p>
            <a href="/api/tasks?format=xml">All tasks</a>
        </p>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Estimated Hours</th>
                <th>Tasks</th>
            </tr>
            </thead>
            <tbody>
            <xsl:for-each select="topic">
                <tr>
                    <td><xsl:value-of select="id"/></td>
                    <td>
                        <a>
                            <xsl:attribute name="href">/api/topics/<xsl:value-of select="id"/>?format=xml</xsl:attribute>
                            <xsl:value-of select="title"/>
                        </a>
                    </td>
                    <td><xsl:value-of select="description"/></td>
                    <td><xsl:value-of select="estimatedHours"/></td>
                    <td>
                        <xsl:value-of select="count(tasks/task)"/>
                    </td>
                </tr>
            </xsl:for-each>
            </tbody>
        </table>
        </body>
        </html>
    </xsl:template>

    <xsl:template match="topic">
        <html>
        <head>
            <title>Topic: <xsl:value-of select="title"/></title>
            <meta charset="UTF-8"/>
            <style>
                body { font-family: Arial, sans-serif; margin: 1rem 2rem; color: #222; }
                h1 { margin: 0 0 1rem 0; font-size: 1.6rem; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; }
                td { vertical-align: top; }
                th { background: #f3f3f3; text-align: left; }
                tbody tr:nth-child(even) { background: #fafafa; }
                tbody tr:hover { background: #eef6ff; }
                a { text-decoration: none; color: #0b63b6; }
                a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>
        <p><a href="/api/topics?format=xml">&#8592; Back to topics</a> | <a href="/api/tasks?format=xml">All tasks</a></p>
        <h1><xsl:value-of select="title"/></h1>
        <p><strong>ID:</strong> <xsl:value-of select="id"/></p>
        <p><strong>Description:</strong> <xsl:value-of select="description"/></p>
        <p><strong>Estimated Hours:</strong> <xsl:value-of select="estimatedHours"/></p>

        <h2>Tasks</h2>
        <table>
            <thead>
            <tr><th>ID</th><th>Title</th><th>Status</th></tr>
            </thead>
            <tbody>
            <xsl:for-each select="tasks/task">
                <tr>
                    <td><xsl:value-of select="id"/></td>
                    <td>
                        <a>
                            <xsl:attribute name="href">/api/tasks/<xsl:value-of select="id"/>?format=xml</xsl:attribute>
                            <xsl:value-of select="title"/>
                        </a>
                    </td>
                    <td><xsl:value-of select="completionStatus"/></td>
                </tr>
            </xsl:for-each>
            </tbody>
        </table>
        </body>
        </html>
    </xsl:template>

    <xsl:template match="tasks">
        <html>
        <head>
            <title>Tasks</title>
            <meta charset="UTF-8"/>
            <style>
                body { font-family: Arial, sans-serif; margin: 1rem 2rem; color: #222; }
                h1 { margin: 0 0 1rem 0; font-size: 1.6rem; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; }
                td { vertical-align: top; }
                th { background: #f3f3f3; text-align: left; }
                tbody tr:nth-child(even) { background: #fafafa; }
                tbody tr:hover { background: #eef6ff; }
                a { text-decoration: none; color: #0b63b6; }
                a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>
        <p><a href="/api/topics?format=xml">All topics</a></p>
        <h1>Tasks</h1>
        <table>
            <thead>
            <tr><th>ID</th><th>Title</th><th>Status</th><th>Topic</th></tr>
            </thead>
            <tbody>
            <xsl:for-each select="task">
                <tr>
                    <td><xsl:value-of select="id"/></td>
                    <td>
                        <a>
                            <xsl:attribute name="href">/api/tasks/<xsl:value-of select="id"/>?format=xml</xsl:attribute>
                            <xsl:value-of select="title"/>
                        </a>
                    </td>
                    <td><xsl:value-of select="completionStatus"/></td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="topicId">
                                <a>
                                    <xsl:attribute name="href">/api/topics/<xsl:value-of select="topicId"/>?format=xml</xsl:attribute>
                                    Topic <xsl:value-of select="topicId"/>
                                </a>
                            </xsl:when>
                            <xsl:otherwise>N/A</xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:for-each>
            </tbody>
        </table>
        </body>
        </html>
    </xsl:template>

    <xsl:template match="task">
        <html>
        <head>
            <title>Task: <xsl:value-of select="title"/></title>
            <meta charset="UTF-8"/>
            <style>
                body { font-family: Arial, sans-serif; margin: 1rem 2rem; color: #222; }
                h1 { margin: 0 0 1rem 0; font-size: 1.6rem; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; }
                td { vertical-align: top; }
                th { background: #f3f3f3; text-align: left; }
                tbody tr:nth-child(even) { background: #fafafa; }
                tbody tr:hover { background: #eef6ff; }
                a { text-decoration: none; color: #0b63b6; }
                a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>
        <p><a href="/api/tasks?format=xml">&#8592; Back to tasks</a> | <a href="/api/topics?format=xml">All topics</a></p>
        <h1><xsl:value-of select="title"/></h1>
        <p><strong>ID:</strong> <xsl:value-of select="id"/></p>
        <p><strong>Status:</strong> <xsl:value-of select="completionStatus"/></p>
        <p><strong>Description:</strong> <xsl:value-of select="description"/></p>
        <p>
            <strong>Topic:</strong>
            <xsl:choose>
                <xsl:when test="topicId">
                    <a>
                        <xsl:attribute name="href">/api/topics/<xsl:value-of select="topicId"/>?format=xml</xsl:attribute>
                        Topic <xsl:value-of select="topicId"/>
                    </a>
                </xsl:when>
                <xsl:otherwise>N/A</xsl:otherwise>
            </xsl:choose>
        </p>
        </body>
        </html>
    </xsl:template>

</xsl:stylesheet>

