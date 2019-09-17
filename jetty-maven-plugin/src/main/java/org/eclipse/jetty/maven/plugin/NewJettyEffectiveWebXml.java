//
//  ========================================================================
//  Copyright (c) 1995-2019 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//


package org.eclipse.jetty.maven.plugin;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.jetty.util.StringUtil;

/**
 * Generate the effective web.xml for a pre-built webapp. This goal will NOT
 * first build the webapp, it must already exist.
 * 
 *
 */
@Mojo(name = "neweffective-web-xml", requiresDependencyResolution = ResolutionScope.RUNTIME)
public class NewJettyEffectiveWebXml extends AbstractWebAppMojo
{
    /**
     * The name of the file to generate into
     */
    @Parameter (defaultValue="${project.build.directory}/effective-web.xml")
    protected File effectiveWebXml;
    
    @Override
    public void configureWebApp() throws Exception
    {
        //TODO consider if we want to be able to generate for the unassembled webapp: so that we could
        //bind this into a build phase, and have it generate the quickstart
        if (StringUtil.isBlank(webApp.getWar()))
            throw new MojoExecutionException("No war specified");

        super.configureWebApp();
    }

    @Override
    protected void startJettyEmbedded() throws MojoExecutionException
    {
       generate();
    }

    /**
     *
     */
    @Override
    protected void startJettyForked() throws MojoExecutionException
    {
       generate();
    }

    /**
     *
     */
    @Override
    protected void startJettyDistro() throws MojoExecutionException
    {
        generate();
    }

    private void generate() throws MojoExecutionException
    {
        try
        {
            QuickStartGenerator generator = new QuickStartGenerator(effectiveWebXml, webApp);
            generator.generate();
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Error generating effective web xml", e);
        }
    }
}
