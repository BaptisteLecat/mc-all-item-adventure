import {google} from 'googleapis';
import {Request, Response} from 'express';

const {defineInt, defineString} = require('firebase-functions/params');

// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
const {logger} = require("firebase-functions");
const {onRequest} = require("firebase-functions/v2/https");

const maxInstancesConfig = defineInt('FN_MAX_INSTANCES', {defaultValue: 0});
const regionConfig = defineString('FN_REGION', {defaultValue: 'europe-west1'});

export const stopServer =

    onRequest(
        {
            maxInstances: maxInstancesConfig,
            region: regionConfig,
        },
        async (req: Request, res: Response) => {
            try {
                const project = process.env.VM_GCP_PROJECT!;
                const zone = process.env.VM_GCP_ZONE!;
                const instance = process.env.VM_INSTANCE_NAME!;

                logger.info(`Stopping instance ${instance} in project ${project} and zone ${zone}`);

                const auth = await google.auth.getClient({
                    scopes: ['https://www.googleapis.com/auth/cloud-platform'],
                });
                const compute = google.compute({version: 'v1', auth});

                // Appel de l’API pour arrêter l’instance
                const result = await compute.instances.stop({
                    project,
                    zone,
                    instance,
                });

                logger.info(`Instance ${instance} stopping: ${JSON.stringify(result.data)}`);
                res.status(200).send(`Instance ${instance} stopping: ${JSON.stringify(result.data)}`);
            } catch (e) {
                logger.error(e);
                res.status(500).send(
                    {
                        error: {
                            code: 500,
                            message: "Internal Server Error",
                        }
                    }
                );
            }
        }
    );

export const startServer = onRequest(
    {
        maxInstances: maxInstancesConfig,
        region: regionConfig,
    },
    async (req: Request, res: Response) => {
        try {
            const project = process.env.VM_GCP_PROJECT!;
            const zone = process.env.VM_GCP_ZONE!;
            const instance = process.env.VM_INSTANCE_NAME!;

            logger.info(`Starting instance ${instance} in project ${project} and zone ${zone}`);

            const auth = await google.auth.getClient({
                scopes: ['https://www.googleapis.com/auth/cloud-platform'],
            });
            const compute = google.compute({version: 'v1', auth});

            // Appel de l’API pour démarrer l’instance
            const result = await compute.instances.start({
                project,
                zone,
                instance,
            });

            logger.info(`Instance ${instance} starting: ${JSON.stringify(result.data)}`);
            res.status(200).send(`Instance ${instance} starting: ${JSON.stringify(result.data)}`);
        } catch (e) {
            logger.error(e);
            res.status(500).send(
                {
                    error: {
                        code: 500,
                        message: "Internal Server Error",
                    }
                }
            );
        }
    }
);
