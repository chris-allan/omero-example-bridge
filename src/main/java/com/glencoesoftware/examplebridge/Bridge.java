/*
 *   Copyright 2016 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE
 */

package com.glencoesoftware.examplebridge;

import ome.model.containers.Project;
import ome.services.fulltext.BridgeHelper;
import ome.util.checksum.ChecksumProvider;
import ome.util.checksum.ChecksumProviderFactory;
import ome.util.checksum.ChecksumProviderFactoryImpl;
import ome.util.checksum.ChecksumType;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;

public class Bridge extends BridgeHelper {

    /* (non-Javadoc)
     * @see ome.services.fulltext.BridgeHelper#set(java.lang.String, java.lang.Object, org.apache.lucene.document.Document, org.hibernate.search.bridge.LuceneOptions)
     */
    @Override
    public void set(final String name, final Object value,
            final Document document, final LuceneOptions _opts) {

        if (value instanceof Project) {

            logger().info(
                "Inserting CRC32 hash of Project name for " + value
            );

            final Project project = (Project) value;
            final ChecksumProviderFactory factory =
                new ChecksumProviderFactoryImpl();
            ChecksumProvider checksumProvider =
                factory.getProvider(ChecksumType.ADLER32);
            try {
              add(
                  document,
                  "name_hash",
                  checksumProvider.putBytes(
                      project.getName().getBytes("UTF-8")
                  ).checksumAsString(),
                  _opts
              );
            } catch (Exception e) {
              logger().warn("Exception while adding name hash to document");
            }
        }
    }
}
